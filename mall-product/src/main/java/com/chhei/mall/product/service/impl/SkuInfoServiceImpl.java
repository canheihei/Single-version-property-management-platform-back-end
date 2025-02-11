package com.chhei.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.chhei.common.utils.R;
import com.chhei.mall.product.entity.SkuImagesEntity;
import com.chhei.mall.product.entity.SpuInfoDescEntity;
import com.chhei.mall.product.fegin.SeckillFeignService;
import com.chhei.mall.product.service.*;
import com.chhei.mall.product.vo.SeckillVO;
import com.chhei.mall.product.vo.SkuItemSaleAttrVo;
import com.chhei.mall.product.vo.SpuItemGroupAttrVo;
import com.chhei.mall.product.vo.SpuItemVO;
import org.apache.skywalking.apm.toolkit.trace.Tag;
import org.apache.skywalking.apm.toolkit.trace.Tags;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chhei.common.utils.PageUtils;
import com.chhei.common.utils.Query;

import com.chhei.mall.product.dao.SkuInfoDao;
import com.chhei.mall.product.entity.SkuInfoEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    SkuInfoDao skuInfoDao;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    SeckillFeignService seckillFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * SKU 信息检索的方法
     * 类别
     * 品牌
     * 价格区间
     * 检索的关键字
     * 分页查询
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        // 检索关键字
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and(w->{
               w.eq("sku_id",key).or().like("sku_name",key);
            });
        }

        // 分类
        String catalogId = (String)params.get("catalogId");
        if(!StringUtils.isEmpty(catalogId) && !"0".equalsIgnoreCase(catalogId)){
            wrapper.eq("catalog_id",catalogId);
        }
        // 品牌
        String brandId = (String)params.get("brandId");
        if(!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        // 价格区间
        String min = (String) params.get("min");
        if(!StringUtils.isEmpty(min)){
            wrapper.ge("price",min);
        }
        String max = (String) params.get("max");
        if(!StringUtils.isEmpty(max)){
            try {
                // 如果max=0那么我们也不需要加这个条件
                BigDecimal bigDecimal = new BigDecimal(max);
                if(bigDecimal.compareTo(new BigDecimal(0)) == 1){
                    // 说明 max > 0
                    wrapper.le("price",max);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    /**
     * 根据spu查询所有的sku信息
     * @param spuId
     * @return
     */
    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        List<SkuInfoEntity> list = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return list;
    }

    @Trace
    @Tags({
            @Tag(key="item",value = "returnedObj")
            ,@Tag(key="itemParam",value = "arg[0]")
    })
    @Override
    public SpuItemVO item(Long skuId) throws ExecutionException, InterruptedException {
        SpuItemVO vo = new SpuItemVO();
        CompletableFuture<SkuInfoEntity> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            // 1.sku的基本信息 pms_sku_info
            SkuInfoEntity skuInfoEntity = getById(skuId);
            vo.setInfo(skuInfoEntity);

            return skuInfoEntity;
        }, threadPoolExecutor);

        CompletableFuture<Void> saleFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            // 3.获取spu中的销售属性的组合
            List<SkuItemSaleAttrVo> saleAttrs = skuSaleAttrValueService
                    .getSkuSaleAttrValueBySpuId(res.getSpuId());
            vo.setSaleAttrs(saleAttrs);
        }, threadPoolExecutor);

        CompletableFuture<Void> spuFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            // 4.获取SPU的介绍
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
            vo.setDesc(spuInfoDescEntity);
        }, threadPoolExecutor);


        CompletableFuture<Void> groupFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            // 5.获取SPU的规格参数
            List<SpuItemGroupAttrVo> groupAttrVo = attrGroupService
                    .getAttrgroupWithSpuId(res.getSpuId(), res.getCatalogId());
            vo.setBaseAttrs(groupAttrVo);
        }, threadPoolExecutor);

        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
            // 2.sku的图片信息pms_sku_images
            List<SkuImagesEntity> images = skuImagesService.getImagesBySkuId(skuId);
            vo.setImages(images);
        }, threadPoolExecutor);

        CompletableFuture<Void> seckillFuture = CompletableFuture.runAsync(() -> {
            // 查询商品的秒杀活动
            R r = seckillFeignService.getSeckillSessionBySkuId(skuId);
            if(r.getCode() == 0){
                SeckillVO seckillVO = JSON.parseObject(r.get("data").toString(),SeckillVO.class);
                vo.setSeckillVO(seckillVO);
            }
        }, threadPoolExecutor);


        CompletableFuture.allOf(saleFuture,spuFuture,imageFuture,groupFuture,seckillFuture).get();
        return vo;
    }

    @Override
    public List<String> getSkuSaleAttrs(Long skuId) {

        return this.skuInfoDao.getSkuSaleAttrs(skuId);
    }

}