package com.chhei.common.dto.es;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品上架的时候需要保存到ES中的信息
 */
@Data
public class SkuESModel {
    private Long skuId;
    private Long spuId;
    private String subTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long catalogId;
    private String brandName;
    private String brandImg;
    private String catalogName;
    private List<Attrs> attrs;

    @Data
    public static class Attrs{
       private Long attrId;
       private String attrName;
       private String attrValue;
    }

}
