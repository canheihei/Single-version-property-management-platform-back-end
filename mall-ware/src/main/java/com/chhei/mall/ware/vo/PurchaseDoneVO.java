package com.chhei.mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * 采购单的VO数据
 */
@Data
public class PurchaseDoneVO {

    private Long id;

    private List<PurchaseItemDoneVO> items;

}
