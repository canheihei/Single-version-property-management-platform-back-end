package com.chhei.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeckillVO {


    private Long promotionSessionId;


    private Long skuId;

    private BigDecimal seckillPrice;

    private BigDecimal seckillCount;

    private BigDecimal seckillLimit;

    private Integer seckillSort;



    private Long startTime;
    private Long endTime;
    // 随机码
    private String randCode;
}
