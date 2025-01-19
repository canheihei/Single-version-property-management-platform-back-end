package com.chhei.mall.ware.service.impl;

import com.chhei.common.dto.SkuHasStockDto;
import com.chhei.common.exception.NoStockExecption;
import com.chhei.common.utils.R;
import com.chhei.mall.ware.feign.ProductFeignService;
import com.chhei.mall.ware.vo.LockStockResult;
import com.chhei.mall.ware.vo.OrderItemVo;
import com.chhei.mall.ware.vo.WareSkuLockVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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

import com.chhei.mall.ware.dao.WareSkuDao;
import com.chhei.mall.ware.entity.WareSkuEntity;
import com.chhei.mall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Autowired
    private WareSkuDao skuDao;

    @Autowired
    private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        // 查询的条件 skuId=10&wareId=1
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if(!StringUtils.isEmpty(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }
        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 入库操作
     * @param skuId 商品编号
     * @param wareId 仓库编号
     * @param skuNum  采购商品的数量
     */
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        // 判断是否有改商品和仓库的入库记录
        List<WareSkuEntity> list = skuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if(list == null || list.size() == 0){
            // 如果没有就新增商品库存记录
            WareSkuEntity entity = new WareSkuEntity();
            entity.setSkuId(skuId);
            entity.setWareId(wareId);
            entity.setStock(skuNum);
            entity.setStockLocked(0);
            try {
                // 动态的设置商品的名称
                R info = productFeignService.info(skuId); // 通过Feign远程调用商品服务的接口
                Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");
                if(info.getCode() == 0){
                    entity.setSkuName((String) data.get("skuName"));
                }
            }catch (Exception e){

            }
            skuDao.insert(entity); // 插入商品库存记录
        }else{
            // 如果有就更新库存
            skuDao.addStock(skuId,wareId,skuNum);
        }
    }

    @Override
    public List<SkuHasStockDto> getSkusHasStock(List<Long> skuIds) {
        log.info("查询库存的 SKU IDs: {}", skuIds);

        List<SkuHasStockDto> list = skuIds.stream().map(skuId -> {
            Long count = baseMapper.getSkuStock(skuId);

            // 防御性处理：确保 `count` 不为 null，避免异常
            if (count == null) {
                log.warn("SKU ID: {} 查询库存返回 null，设置为 0", skuId);
                count = 0L;
            }

            log.info("SKU ID: {}, 库存数量: {}", skuId, count);

            SkuHasStockDto dto = new SkuHasStockDto();
            dto.setSkuId(skuId);
            dto.setHasStock(count > 0); // 库存大于 0 时才设置为有库存

            return dto;
        }).collect(Collectors.toList());

        log.info("返回的库存信息: {}", list);
        return list;
    }

    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLockVO vo) {
        List<OrderItemVo> items = vo.getItems();
        // 首先找到具有库存的仓库
        List<SkuWareHasStock> collect = items.stream().map(item -> {
            SkuWareHasStock skuWareHasStock = new SkuWareHasStock();
            skuWareHasStock.setSkuId(item.getSkuId());
            List<WareSkuEntity> wareSkuEntities = this.baseMapper.listHashStock(item.getSkuId());
            skuWareHasStock.setWareSkuEntities(wareSkuEntities);
            skuWareHasStock.setNum(item.getCount());
            return skuWareHasStock;
        }).collect(Collectors.toList());
        // 尝试锁定库存
        for (SkuWareHasStock skuWareHasStock : collect) {
            Long skuId = skuWareHasStock.getSkuId();
            List<WareSkuEntity> wareSkuEntities = skuWareHasStock.wareSkuEntities;
            if(wareSkuEntities == null && wareSkuEntities.size() == 0){
                // 当前商品没有库存了
                throw new NoStockExecption(skuId);
            }
            // 当前需要锁定的商品的梳理
            Integer count = skuWareHasStock.getNum();
            Boolean skuStocked = false; // 表示当前SkuId的库存没有锁定完成
            for (WareSkuEntity wareSkuEntity : wareSkuEntities) {
                // 循环获取到对应的 仓库，然后需要锁定库存
                // 获取当前仓库能够锁定的库存数
                Integer canStock = wareSkuEntity.getStock() - wareSkuEntity.getStockLocked();
                if(count <= canStock){
                    // 表示当前的skuId的商品的数量小于等于需要锁定的数量
                    Integer i = this.baseMapper.lockSkuStock(skuId,wareSkuEntity.getWareId(),count);
                    count = 0;
                    skuStocked = true;
                }else{
                    // 需要锁定的库存大于 可以锁定的库存 就按照已有的库存来锁定
                    Integer i = this.baseMapper.lockSkuStock(skuId,wareSkuEntity.getWareId(),canStock);
                    count = count - canStock;
                }
                if(count <= 0 ){
                    // 表示所有的商品都锁定了
                    break;
                }
            }
            if(count > 0){
                // 说明库存没有锁定完
                throw new NoStockExecption(skuId);
            }
            if(skuStocked == false){
                // 表示上一个商品的没有锁定库存成功
                throw new NoStockExecption(skuId);
            }
        }
        return true;
    }

    @Data
    class SkuWareHasStock{
        private Long skuId;
        private Integer num;
        private List<WareSkuEntity> wareSkuEntities;
    }
}