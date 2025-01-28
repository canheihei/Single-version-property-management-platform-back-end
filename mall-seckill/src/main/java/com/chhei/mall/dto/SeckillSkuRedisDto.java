package com.chhei.mall.dto;

import com.chhei.mall.vo.SkuInfoVo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 封装SKU相关信息，保存到Redis中的传输对象
 */
@Data
public class SeckillSkuRedisDto {


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

    private SkuInfoVo skuInfoVo;

}
