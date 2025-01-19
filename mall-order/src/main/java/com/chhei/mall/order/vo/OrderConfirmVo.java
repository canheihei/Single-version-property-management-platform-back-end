package com.chhei.mall.order.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单确认页面的中的数据VO
 */

public class OrderConfirmVo {

    // 订单的收货人 及 收货地址信息
    @Getter @Setter
    List<MemberAddressVo> address;
    // 购物车中选中的商品信息
    @Getter @Setter
    List<OrderItemVo> items;
    // 支付方式
    // 发票信息
    // 优惠信息

    //Integer countNum;

    @Getter @Setter
    private String orderToken;

    public Integer getCountNum() {
        int count = 0;
        if (items != null) {
            for (OrderItemVo item : items) {
                count += item.getCount();
            }
        }
        return count;
    }

    // BigDecimal total ;// 总的金额
    public BigDecimal getTotal(){
        BigDecimal sum = new BigDecimal(0);
        if(items != null ){
            for (OrderItemVo item : items) {
                BigDecimal totalPrice = item.getPrice().multiply(new BigDecimal(item.getCount()));
                sum = sum.add(totalPrice);
            }
        }
        return sum;
    }
    // BigDecimal payTotal;// 需要支付的总金额
    public BigDecimal getPayTotal(){
        return getTotal();
    }
}
