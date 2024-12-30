package com.chhei.mall.vo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * 购物车
 */
public class Cart {
    // 购物车中的商品种类
    private Integer countType;

    // 选中的商品数量
    private Integer checkCountNum;

    // 选中商品的总价
    private BigDecimal totalAmount;

    // 购物中存储的商品信息
    private List<CartItem> items;

    public Integer getCountType() {
        return items.size();
    }


    public Integer getCheckCountNum() {
        Integer count = 0;
        for (CartItem item : items) {
            if (item.isCheck()){
                count += item.getCount();
            }
        }
        return count;
    }


    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal(0);
        for (CartItem item : items) {
            if (item.isCheck()){
                amount = amount.add(item.getTotalPrice());
            }
        }
        return amount;
    }


    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
