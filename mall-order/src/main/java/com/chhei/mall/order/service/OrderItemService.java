package com.chhei.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chhei.common.utils.PageUtils;
import com.chhei.mall.order.entity.OrderItemEntity;

import java.util.Map;

/**
 * 订单项信息
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-14 14:11:20
 */
public interface OrderItemService extends IService<OrderItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

