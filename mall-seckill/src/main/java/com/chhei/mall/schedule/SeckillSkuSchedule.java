package com.chhei.mall.schedule;

import com.chhei.mall.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 定时上架秒杀商品信息
 */
@Slf4j
@Component
public class SeckillSkuSchedule {

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedissonClient redissonClient;

    /**
     *
     */

    @Async
    @Scheduled(cron = "*/5 * * * * *")
    public void uploadSeckillSku3Days(){
        log.info("定时上架秒杀商品执行了...." + new Date());
        // 分布式锁
        RLock lock = redissonClient.getLock("seckill:upload:lock");
        lock.lock(10, TimeUnit.SECONDS);
        try {
            // 调用上架商品的方法
            seckillService.uploadSeckillSku3Days();
        }catch (Exception e){

        }finally {
            lock.unlock();
        }
    }
}
