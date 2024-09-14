package com.chhei.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chhei.common.utils.PageUtils;
import com.chhei.mall.coupon.entity.MemberPriceEntity;

import java.util.Map;

/**
 * 商品会员价格
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-14 14:12:06
 */
public interface MemberPriceService extends IService<MemberPriceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

