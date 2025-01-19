package com.chhei.mall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@EnableFeignClients(basePackages = "com.chhei.mall.order.fegin")
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.chhei.mall.order.dao")
@EnableAspectJAutoProxy(exposeProxy = true)
public class MallOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallOrderApplication.class, args);
	}

}
