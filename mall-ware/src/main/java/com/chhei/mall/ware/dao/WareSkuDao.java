package com.chhei.mall.ware.dao;

import com.chhei.mall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 * 
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-14 14:08:28
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

	void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

	Long getSkuStock(Long skuId);

	List<WareSkuEntity> listHashStock(Long skuId);

	Integer lockSkuStock(@Param("skuId") Long skuId,@Param("wareId") Long wareId,@Param("count") Integer count);
}
