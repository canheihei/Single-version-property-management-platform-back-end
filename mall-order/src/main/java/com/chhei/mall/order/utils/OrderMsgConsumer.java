package com.chhei.mall.order.utils;

import com.chhei.common.constant.OrderConstant;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(topic = OrderConstant.ROCKETMQ_ORDER_TOPIC,consumerGroup = "${rocketmq.consumer.group}")
@Component
public class OrderMsgConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        // 订单关单的逻辑实现
        System.out.println("收到的消息:"+ s );
    }
}
