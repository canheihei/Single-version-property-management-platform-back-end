package com.chhei.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

//放开注册中心
@EnableFeignClients(basePackages = "com.chhei.mall.product.fegin")
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.chhei.mall.product.dao")
@ComponentScan(basePackages = "com.chhei")
public class MallProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallProductApplication.class, args);
	}

}
