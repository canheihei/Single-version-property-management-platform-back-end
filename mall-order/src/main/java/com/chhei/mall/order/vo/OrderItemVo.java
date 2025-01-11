package com.chhei.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemVo {
    // 商品的编号 SkuId
    private Long skuId;

    // SPU编号
    private Long spuId;

    // 商品的图片
    private String image;
    // 商品的标题
    private String title;

    private boolean check;
    // 商品的销售属性
    private List<String> skuAttr;
    // 商品的单价
    private BigDecimal price;
    // 购买的数量
    private Integer count;
    // 商品的总价
    private BigDecimal totalPrice;

    // 商品是否有货
    private boolean hasStack = true;
}
