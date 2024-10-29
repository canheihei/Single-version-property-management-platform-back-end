package com.chhei.mall.ware.service.impl;

import com.chhei.common.constant.WareConstant;
import com.chhei.mall.ware.entity.PurchaseDetailEntity;
import com.chhei.mall.ware.service.PurchaseDetailService;
import com.chhei.mall.ware.service.WareSkuService;
import com.chhei.mall.ware.vo.MergeVO;
import com.chhei.mall.ware.vo.PurchaseDoneVO;
import com.chhei.mall.ware.vo.PurchaseItemDoneVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chhei.common.utils.PageUtils;
import com.chhei.common.utils.Query;
import com.chhei.mall.ware.dao.PurchaseDao;
import com.chhei.mall.ware.entity.PurchaseEntity;
import com.chhei.mall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService detailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();
        // 添加查询的条件
        queryWrapper.eq("status",0).or().eq("status",1);
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public Integer merge(MergeVO mergeVO) {
        Long purchaseId = mergeVO.getPurchaseId();
        if(purchaseId == null){
            // 新建采购单
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        // 判断采购单的状态 只能是新建或者已分配的才能合单 如果是已领取 就不能再合单了
        PurchaseEntity purchaseEntity = this.getById(purchaseId);
        if(purchaseEntity.getStatus() > WareConstant.PurchaseStatusEnum.RECEIVE.getCode()){
            // 该菜单不能合单
            return -1;
        }

        // 整合菜单需求单
        List<Long> items = mergeVO.getItems();
        final long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> list = items.stream().map(i -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            // 更新每一条 需求单的 采购单编号
            detailEntity.setId(i);
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGED.getCode());
            return detailEntity;
        }).filter(id ->{
            PurchaseDetailEntity item = detailService.getById(id);
            if(item.getStatus() == WareConstant.PurchaseDetailStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseDetailStatusEnum.ASSIGED.getCode()){
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if(list != null && list.size() > 0){
            detailService.updateBatchById(list);
        }
        // 更新对应的采购单的更新时间
        PurchaseEntity entity = new PurchaseEntity();
        entity.setId(purchaseId);
        entity.setUpdateTime(new Date());
        this.updateById(entity);
        return 1;
    }

    @Transactional
    @Override
    public void received(List<Long> ids) {
        // 1.领取的采购单的状态只能是新建或者已分配的采购单 其他的是不能领取的
        List<PurchaseEntity> list = ids.stream().map(id -> {
            return this.getById(id);
        }).filter(item -> {

            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGED.getCode()) {

                return true;
            }
            return false;
        }).map(item->{
            item.setUpdateTime(new Date()); // 设置更新时间
            // 更新采购单的状态为 已领取
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            return item;
        }).collect(Collectors.toList());
        // 2.更新采购单的状态为 已领取
        this.updateBatchById(list);
        // 3.更新采购项的状态为 正在采购

        for (Long id : ids) {
            // 根据采购单id 找到对应的采购项对象
            List<PurchaseDetailEntity> detailEntities = detailService.listDetailByPurchaseId(id);
            List<PurchaseDetailEntity> collect = detailEntities.stream().map(item -> {
                PurchaseDetailEntity entity = new PurchaseDetailEntity();
                entity.setId(item.getId());
                entity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return entity;
            }).collect(Collectors.toList());
            // 批量更新采购项
            detailService.updateBatchById(collect);
        }
    }

    @Transactional
    @Override
    public void done(PurchaseDoneVO vo) {
        // 获取采购单编号
        Long id = vo.getId();
        // 2.改变采购项的状态
        Boolean flag = true; // 记录采购的状态 默认为 完成
        // 获取所有的采购项
        List<PurchaseItemDoneVO> items = vo.getItems();
        List<PurchaseDetailEntity> list = new ArrayList<>();
        for (PurchaseItemDoneVO item : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if(item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()){
                // 该采购项采购出现了问题
                flag = false;
                detailEntity.setStatus(item.getStatus());
            }else{
                // 采购项采购成功
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                // 3.将采购成功的采购项进入库操作
                // 跟进采购项编号查询出对应的采购项详情
                PurchaseDetailEntity detailEntity1 = detailService.getById(item.getItemId());
                wareSkuService.addStock(detailEntity1.getSkuId(),detailEntity1.getWareId(),detailEntity1.getSkuNum());

            }
            detailEntity.setId(item.getItemId());
            //detailService.updateById(detailEntity);
            list.add(detailEntity);
        }
        detailService.updateBatchById(list); // 批量更新 采购项

        // 1.改变采购单的状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISH.getCode()
                : WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);

    }
}