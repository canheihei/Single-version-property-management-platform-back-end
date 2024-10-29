package com.chhei.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chhei.common.utils.PageUtils;
import com.chhei.mall.ware.entity.PurchaseEntity;
import com.chhei.mall.ware.vo.MergeVO;
import com.chhei.mall.ware.vo.PurchaseDoneVO;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-14 14:08:28
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

	PageUtils queryPageUnreceive(Map<String, Object> params);

	Integer merge(MergeVO mergeVO);

	void received(List<Long> ids);

	void done(PurchaseDoneVO vo);
}

