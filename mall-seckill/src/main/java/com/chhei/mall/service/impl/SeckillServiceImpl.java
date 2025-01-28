package com.chhei.mall.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.chhei.common.constant.OrderConstant;
import com.chhei.common.constant.SeckillConstant;
import com.chhei.common.utils.R;
import com.chhei.common.vo.MemberVO;
import com.chhei.mall.dto.SeckillOrderDto;
import com.chhei.mall.dto.SeckillSkuRedisDto;
import com.chhei.mall.feign.CouponFeignService;
import com.chhei.mall.feign.ProductFeignService;
import com.chhei.mall.interceptor.AuthInterceptor;
import com.chhei.mall.service.SeckillService;
import com.chhei.mall.vo.SeckillSessionEntity;
import com.chhei.mall.vo.SkuInfoVo;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SeckillServiceImpl implements SeckillService {

	@Autowired
	CouponFeignService couponFeignService;

	@Autowired
	RedisTemplate redisTemplate;

	@Autowired
	ProductFeignService productFeignService;

	@Autowired
	RedissonClient redissonClient;

	@Autowired
	RocketMQTemplate rocketMQTemplate;

	@Override
	public void uploadSeckillSku3Days() {
		R r = couponFeignService.getLates3DaysSession();
		if(r.getCode()==0){
			String json = (String) r.get("data");
			List<SeckillSessionEntity> seckSessionEntities = JSON.parseArray(json, SeckillSessionEntity.class);

			saveSessionInfos(seckSessionEntities);

			saveSessionSkuInfos(seckSessionEntities);
		}
	}

	private void saveSessionInfos(List<SeckillSessionEntity> seckillSessionEntities) {
		for (SeckillSessionEntity seckillSessionEntity : seckillSessionEntities) {
			// 循环缓存每一个活动  key： start_endTime
			long start = seckillSessionEntity.getStartTime().getTime();
			long end = seckillSessionEntity.getEndTime().getTime();
			// 生成Key
			String key = SeckillConstant.SESSION_CHACE_PREFIX + start + "_" + end;
			Boolean flag = redisTemplate.hasKey(key);
			if (!flag) {// 表示这个秒杀活动在Redis中不存在，也就是还没有上架，那么需要保存
				// 需要存储到Redis中的这个秒杀活动涉及到的相关的商品信息的SKUID
				List<String> collect = seckillSessionEntity.getRelationEntities().stream().map(item -> {
					// 秒杀活动存储的 VALUE是 sessionId_SkuId
					return item.getPromotionSessionId() + "_" + item.getSkuId().toString();
				}).collect(Collectors.toList());
				redisTemplate.opsForList().leftPushAll(key, collect);
			}
		}
	}

	private void saveSessionSkuInfos(List<SeckillSessionEntity> seckillSessionEntities) {
		seckillSessionEntities.stream().forEach(session -> {
			// 循环取出每个Session，然后取出对应SkuID 封装相关的信息
			BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(SeckillConstant.SKU_CHACE_PREFIX);
			session.getRelationEntities().stream().forEach(item->{
				String skuKey = item.getPromotionSessionId()+"_"+item.getSkuId();
				Boolean flag = hashOps.hasKey(skuKey);
				if(!flag){
					SeckillSkuRedisDto dto = new SeckillSkuRedisDto();
					// 1.获取SKU的基本信息
					R info = productFeignService.info(item.getSkuId());
					if(info.getCode() == 0){
						// 表示查询成功
						String json = (String) info.get("skuInfoJSON");
						dto.setSkuInfoVo(JSON.parseObject(json, SkuInfoVo.class));
					}
					// 2.获取SKU的秒杀信息
                    /*dto.setSkuId(item.getSkuId());
                    dto.setSeckillPrice(item.getSeckillPrice());
                    dto.setSeckillCount(item.getSeckillCount());
                    dto.setSeckillLimit(item.getSeckillLimit());
                    dto.setSeckillSort(item.getSeckillSort());*/
					BeanUtils.copyProperties(item,dto);
					// 3.设置当前商品的秒杀时间
					dto.setStartTime(session.getStartTime().getTime());
					dto.setEndTime(session.getEndTime().getTime());

					// 4. 随机码
					String token = UUID.randomUUID().toString().replace("-","");
					dto.setRandCode(token);
					// 绑定对应的 活动编号
					dto.setPromotionSessionId(item.getPromotionSessionId());
					// 分布式信号量的处理  限流的目的
					RSemaphore semaphore = redissonClient.getSemaphore(SeckillConstant.SKU_STOCK_SEMAPHORE + token);
					// 把秒杀活动的商品数量作为分布式信号量的信号量
					semaphore.trySetPermits(item.getSeckillCount().intValue());
					hashOps.put(skuKey,JSON.toJSONString(dto));
				}
			});
		});
	}

	@Override
	public List<SeckillSkuRedisDto> getCurrentSeckillSkus() {
		Long time = new Date().getTime();
		Set<String> keys = redisTemplate.keys(SeckillConstant.SESSION_CHACE_PREFIX + "*");
		for (String key : keys) {
			String replace = key.replace(SeckillConstant.SESSION_CHACE_PREFIX, "");
			String[] s = replace.split("_");
			Long start = Long.parseLong(s[0]);
			Long end = Long.parseLong(s[1]);
			if(time > start && time < end){
				// 说明的秒杀活动就是当前时间需要参与的活动
				// 取出来的是SKU的ID  2_9
				List<String> range = redisTemplate.opsForList().range(key, -100, 100);
				BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SeckillConstant.SKU_CHACE_PREFIX);
				List<String> list = ops.multiGet(range);
				if(list != null && list.size() > 0){
					List<SeckillSkuRedisDto> collect = list.stream().map(item -> {
						SeckillSkuRedisDto seckillSkuRedisDto = JSON.parseObject(item, SeckillSkuRedisDto.class);
						return seckillSkuRedisDto;
					}).collect(Collectors.toList());
					return collect;
				}
			}

		}

		return null;
	}

	@Override
	public SeckillSkuRedisDto getSeckillSessionBySkuId(Long skuId) {
		// 1.找到所有需要参与秒杀的商品的sku信息
		BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SeckillConstant.SKU_CHACE_PREFIX);
		Set<String> keys = ops.keys();
		if(keys != null && keys.size() > 0){
			String regx = "\\d_"+ skuId; // 2_1
			for (String key : keys) {
				boolean matches = Pattern.matches(regx, key);
				if(matches){
					// 说明找到了对应的SKU的信息
					String json = ops.get(key);
					SeckillSkuRedisDto dto = JSON.parseObject(json, SeckillSkuRedisDto.class);
					return dto;
				}
			}
		}
		return null;
	}

	@Override
	public String kill(String killId, String code, Integer num) {
		// 1.根据killId获取当前秒杀的商品的信息  Redis中
		BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SeckillConstant.SKU_CHACE_PREFIX);
		String json = ops.get(killId);
		if(StringUtils.isNotBlank(json)){
			SeckillSkuRedisDto dto = JSON.parseObject(json, SeckillSkuRedisDto.class);
			// 校验合法性  1.校验时效性
			Long startTime = dto.getStartTime();
			Long endTime = dto.getEndTime();
			long now = new Date().getTime();
			if(now > startTime && now < endTime){
				// 说明是在秒杀活动时间范围内容的请求
				// 2.校验 随机和商品 是否合法
				String randCode = dto.getRandCode();
				Long skuId = dto.getSkuId();
				String redisKillId = dto.getPromotionSessionId() + "_" + skuId;
				if(randCode.equals(code) && killId.equals(redisKillId)){
					// 随机码校验合法
					// 3.判断抢购商品数量是否合法
					if(num <= dto.getSeckillLimit().intValue()){
						// 满足限购的条件
						// 4.判断是否满足 幂等性
						// 只要抢购成功我们就在Redis中 存储一条信息 userId + sessionID + skuId
						MemberVO memberVO = (MemberVO) AuthInterceptor.threadLocal.get();
						Long id = memberVO.getId();
						String redisKey = id + "_" + redisKillId;
						Boolean aBoolean = redisTemplate.opsForValue()
								.setIfAbsent(redisKey, num.toString(), (endTime - now), TimeUnit.MILLISECONDS);
						if(aBoolean){
							// 表示数据插入成功 是第一次操作
							RSemaphore semaphore = redissonClient.getSemaphore(SeckillConstant.SKU_STOCK_SEMAPHORE+randCode);
							try {
								boolean b = semaphore.tryAcquire(num, 100, TimeUnit.MILLISECONDS);
								if(b){
									// 表示秒杀成功
									String orderSN = UUID.randomUUID().toString().replace("-", "");
									// 继续完成快速下订单操作  --> RocketMQ
									SeckillOrderDto orderDto = new SeckillOrderDto() ;
									orderDto.setOrderSN(orderSN);
									orderDto.setSkuId(skuId);
									orderDto.setSeckillPrice(dto.getSeckillPrice());
									orderDto.setMemberId(id);
									orderDto.setNum(num);
									orderDto.setPromotionSessionId(dto.getPromotionSessionId());
									// 通过RocketMQ 发送异步消息
									rocketMQTemplate.sendOneWay(OrderConstant.ROCKETMQ_SECKILL_ORDER_TOPIC
											,JSON.toJSONString(orderDto));
									return orderSN;
								}
							} catch (InterruptedException e) {
								return null;
							}
						}
					}

				}
			}
		}
		return null;
	}
}
