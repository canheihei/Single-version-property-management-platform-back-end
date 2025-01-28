package com.chhei.mall.order.dao;

import com.chhei.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 * 
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-14 14:11:20
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

	OrderEntity getOrderByOrderSn(@Param("orderSn") String orderSn);

	void updateOrderStatus(@Param("orderSn") String orderSn, @Param("status")Integer status);
}
