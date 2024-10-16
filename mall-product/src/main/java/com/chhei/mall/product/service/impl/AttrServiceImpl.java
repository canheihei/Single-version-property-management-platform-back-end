package com.chhei.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chhei.common.constant.ProductConstant;
import com.chhei.mall.product.dao.AttrAttrgroupRelationDao;
import com.chhei.mall.product.entity.AttrAttrgroupRelationEntity;
import com.chhei.mall.product.entity.AttrGroupEntity;
import com.chhei.mall.product.entity.CategoryEntity;
import com.chhei.mall.product.service.AttrAttrgroupRelationService;
import com.chhei.mall.product.service.AttrGroupService;
import com.chhei.mall.product.service.CategoryService;
import com.chhei.mall.product.vo.AttrResponseVo;
import com.chhei.mall.product.vo.AttrVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chhei.common.utils.PageUtils;
import com.chhei.common.utils.Query;

import com.chhei.mall.product.dao.AttrDao;
import com.chhei.mall.product.entity.AttrEntity;
import com.chhei.mall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVO vo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(vo,attrEntity);
        this.save(attrEntity);
        if(vo.getAttrGroupId()!=null && vo.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(vo.getAttrGroupId());
            attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public PageUtils queryBasePage(Map<String, Object> params, Long catelogId, String attrType) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_type","base".equalsIgnoreCase(attrType)?1:0);
        if(catelogId != 0){
            wrapper.eq("catelog_id",catelogId);
        }
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
               w.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrResponseVo> list = records.stream().map((attrEntity) -> {
            AttrResponseVo responseVo = new AttrResponseVo();
            BeanUtils.copyProperties(attrEntity, responseVo);

            CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                responseVo.setCatelogName(categoryEntity.getName());
            }
            if("base".equalsIgnoreCase(attrType)){
                AttrAttrgroupRelationEntity entity = new AttrAttrgroupRelationEntity();
                entity.setAttrId(attrEntity.getAttrId());
                //attrAttrgroupRelationService.query(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attrEntity.getAttrId()));
                AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao
                        .selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attrAttrgroupRelationEntity != null && attrAttrgroupRelationEntity.getAttrGroupId() != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrAttrgroupRelationEntity.getAttrGroupId());
                    responseVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            return responseVo;
        }).collect(Collectors.toList());
        pageUtils.setList(list);
        return pageUtils;
    }

    @Override
    public AttrResponseVo getAttrInfo(Long attrId) {
        AttrResponseVo responseVo = new AttrResponseVo();

        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,responseVo);
        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if(relationEntity != null){
                AttrGroupEntity attrGroupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
                responseVo.setAttrGroupId(attrGroupEntity.getAttrGroupId());
                if(attrGroupEntity != null){
                    responseVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        responseVo.setCatelogPath(catelogPath);
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        if(categoryEntity != null){
            responseVo.setCatelogName(categoryEntity.getName());
        }

        return responseVo;
    }

    @Transactional
    @Override
    public void updateBaseAttr(AttrVO attr) {
        AttrEntity entity = new AttrEntity();
        BeanUtils.copyProperties(attr,entity);

        this.updateById(entity);
        if(entity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(entity.getAttrId());
            relationEntity.setAttrGroupId(attr.getAttrGroupId());

            Integer count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));
            if(count > 0){
                attrAttrgroupRelationDao.update(relationEntity,new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));
            }else{
                attrAttrgroupRelationDao.insert(relationEntity);
            }
        }
    }
}