package com.chhei.mall.product.dao;

import com.chhei.mall.product.entity.SkuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku信息
 * 
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-13 20:32:33
 */
@Mapper
public interface SkuInfoDao extends BaseMapper<SkuInfoEntity> {
	List<String> getSkuSaleAttrs(@Param("skuId") Long skuId);
}
