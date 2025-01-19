package com.chhei.common.exception;

public class NoStockExecption extends RuntimeException{

    private Long skuId;

    public NoStockExecption(Long skuId){
        super("当前商品["+skuId+"]没有库存了");
        this.skuId = skuId;

    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}