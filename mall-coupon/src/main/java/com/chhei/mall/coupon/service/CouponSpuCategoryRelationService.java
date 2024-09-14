package com.chhei.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chhei.common.utils.PageUtils;
import com.chhei.mall.coupon.entity.CouponSpuCategoryRelationEntity;

import java.util.Map;

/**
 * 优惠券分类关联
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-14 14:12:06
 */
public interface CouponSpuCategoryRelationService extends IService<CouponSpuCategoryRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

