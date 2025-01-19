package com.chhei.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chhei.common.dto.SkuHasStockDto;
import com.chhei.common.utils.PageUtils;
import com.chhei.mall.ware.entity.WareSkuEntity;
import com.chhei.mall.ware.vo.LockStockResult;
import com.chhei.mall.ware.vo.WareSkuLockVO;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-14 14:08:28
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void addStock(Long skuId, Long wareId, Integer skuNum);

	List<SkuHasStockDto> getSkusHasStock(List<Long> skuIds);

	Boolean orderLockStock(WareSkuLockVO vo);
}

