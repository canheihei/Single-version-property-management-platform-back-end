package com.chhei.mall.order.vo;

import lombok.Data;

/**
 * 订单结算页提交的信息
 */
@Data
public class OrderSubmitVO {

    // 收获地址的id
    private Long addrId;

    // 支付方式
    private Integer payType;

    // 防重Token
    private String orderToken;

    // 买家备注
    private String note;
}
