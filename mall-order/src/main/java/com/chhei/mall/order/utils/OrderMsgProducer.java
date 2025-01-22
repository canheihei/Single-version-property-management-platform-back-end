package com.chhei.mall.order.utils;

import com.chhei.common.constant.OrderConstant;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class OrderMsgProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendOrderMessage(String orderSN){
        rocketMQTemplate.syncSend(OrderConstant.ROCKETMQ_ORDER_TOPIC, MessageBuilder.withPayload(orderSN).build(),5000,4);
    }
}
