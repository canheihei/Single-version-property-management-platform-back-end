/**
  * Copyright 2022 json.cn 
  */
package com.chhei.mall.product.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

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
public class SpuInfoVO {
    private Long id;
    private String spuName;
    private String spuDescription;
    private long catalogId;
    private String catalogName;
    private long brandId;
    private String brandName;
    private BigDecimal weight;
    private Integer publishStatus;
    private List<String> decript;
    private List<String> images;
    private Bounds bounds;
    private List<BaseAttrs> baseAttrs;
    private List<Skus> skus;
    private Date createTime;
    private Date updateTime;
}