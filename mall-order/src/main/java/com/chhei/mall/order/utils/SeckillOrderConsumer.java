package com.chhei.mall.order.utils;

import com.alibaba.fastjson.JSON;
import com.chhei.common.constant.OrderConstant;
import com.chhei.common.dto.SeckillOrderDto;
import com.chhei.mall.order.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(topic = OrderConstant.ROCKETMQ_SECKILL_ORDER_TOPIC,consumerGroup = "test")
@Component
public class SeckillOrderConsumer implements RocketMQListener<String> {
    @Autowired
    OrderService orderService;
    @Override
    public void onMessage(String s) {
        // 订单关单的逻辑实现
        SeckillOrderDto orderDto = JSON.parseObject(s,SeckillOrderDto.class);
        orderService.quickCreateOrder(orderDto);
    }
}
