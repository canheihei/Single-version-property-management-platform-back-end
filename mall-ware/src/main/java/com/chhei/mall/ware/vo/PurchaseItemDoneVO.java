package com.chhei.mall.ware.vo;

import lombok.Data;

/**
 * 采购项的VO数据
 */
@Data
public class PurchaseItemDoneVO {

    private Long itemId;
    private Integer status;
    private String reason;
}
