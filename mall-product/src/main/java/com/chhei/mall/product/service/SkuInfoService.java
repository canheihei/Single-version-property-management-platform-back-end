package com.chhei.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chhei.common.utils.PageUtils;
import com.chhei.mall.product.entity.SkuInfoEntity;
import com.chhei.mall.product.vo.SpuItemVO;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-13 20:32:33
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage	(Map<String, Object> params);

	PageUtils queryPageByCondition(Map<String, Object> params);

	List<SkuInfoEntity> getSkusBySpuId(Long spuId);

	SpuItemVO item(Long skuId) throws ExecutionException, InterruptedException;

	List<String> getSkuSaleAttrs(Long skuId);
}

