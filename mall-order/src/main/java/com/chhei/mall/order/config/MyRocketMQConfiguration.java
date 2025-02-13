package com.chhei.mall.order.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;

public class MyRocketMQConfiguration {
	@Value("${rocket.nameser}")
	private String nameSer;

	private String orderTopic = "orderTopic";

	public void handler(){

	}

	public void sendDelayMsg (String orderSN)throws Exception{
		// 实例化一个生产者来产生延时消息
		DefaultMQProducer producer = new DefaultMQProducer("ExampleProducerGroup");
		producer.setNamesrvAddr(nameSer);
		// 启动生产者
		producer.start();
		int totalMessagesToSend = 100;
		for (int i = 0; i < totalMessagesToSend; i++) {
			Message message = new Message(orderTopic, ("Hello scheduled message " + i).getBytes());
			// 设置延时等级3,这个消息将在10s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)
			message.setDelayTimeLevel(4);
			// 发送消息
			producer.send(message);
		}
		// 关闭生产者
		producer.shutdown();
	}
}
