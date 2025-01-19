package com.chhei.mall.order.dto;

import com.chhei.mall.order.entity.OrderEntity;
import com.chhei.mall.order.entity.OrderItemEntity;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateTO {

    private OrderEntity orderEntity; // 订单信息
    private List<OrderItemEntity> orderItemEntities; // 订单信息
}
