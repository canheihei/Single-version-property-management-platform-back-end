package com.chhei.mall.ware.vo;

import lombok.Data;

@Data
public class LockStockResult {

    private Long skuId;
    private Integer count;
    private Boolean locked;
}
