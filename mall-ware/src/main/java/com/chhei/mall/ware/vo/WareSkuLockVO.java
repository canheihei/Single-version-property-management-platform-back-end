package com.chhei.mall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class WareSkuLockVO {

    // 订单编号
    private String orderSN;

    private List<OrderItemVo> items;
}
