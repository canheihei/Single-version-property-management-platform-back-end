package com.chhei.mall.service;

import com.chhei.mall.dto.SeckillSkuRedisDto;

import java.util.List;

public interface SeckillService {
	void uploadSeckillSku3Days();

	List<SeckillSkuRedisDto> getCurrentSeckillSkus();

	SeckillSkuRedisDto getSeckillSessionBySkuId(Long skuId);

	String kill(String killId, String code, Integer num);
}
