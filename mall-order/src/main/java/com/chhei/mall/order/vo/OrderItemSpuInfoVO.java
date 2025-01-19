package com.chhei.mall.order.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItemSpuInfoVO implements Serializable {
    private Long id;
    private String spuName;
    private String spuDescription;
    private long catalogId;
    private String catalogName;
    private long brandId;
    private String brandName;
    private String img;
}
