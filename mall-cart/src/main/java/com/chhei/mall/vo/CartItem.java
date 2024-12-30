package com.chhei.mall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车中的商品信息
 */
public class CartItem {

    // 商品的编号 SkuId
    private Long skuId;

    private Long spuId;
    // 商品的图片
    private String image;
    // 商品的标题
    private String title;
    // 是否选中
    private boolean check = true;
    // 商品的销售属性
    private List<String> skuAttr;
    // 商品的单价
    private BigDecimal price;
    // 购买的数量
    private Integer count;
    // 商品的总价
    private BigDecimal totalPrice;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public List<String> getSkuAttr() {
        return skuAttr;
    }

    public void setSkuAttr(List<String> skuAttr) {
        this.skuAttr = skuAttr;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getTotalPrice() {
        // 商品的总价  price * count
        return price.multiply(new BigDecimal(count));
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }
}
