/**
  * Copyright 2022 json.cn 
  */
package com.chhei.mall.product.vo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Auto-generated: 2022-01-18 14:49:13
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
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