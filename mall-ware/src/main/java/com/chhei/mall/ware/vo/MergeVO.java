package com.chhei.mall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class MergeVO {
    //{ purchaseId: this.purchaseId, items: items }
    private Long purchaseId;
    private List<Long> items;
}
