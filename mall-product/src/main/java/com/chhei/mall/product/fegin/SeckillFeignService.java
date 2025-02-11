package com.chhei.mall.product.fegin;

import com.chhei.common.utils.R;
import com.chhei.mall.product.fegin.fallback.SeckillFeignServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "mall-seckill",fallback = SeckillFeignServiceFallback.class)
public interface SeckillFeignService {

    @GetMapping("/seckill/seckillSessionBySkuId")
    R getSeckillSessionBySkuId(@RequestParam("skuId") Long skuId);
}
