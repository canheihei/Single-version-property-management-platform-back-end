package com.chhei.mall.feign;

import com.chhei.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("mall-coupon")
public interface CouponFeignService {

    @GetMapping("/coupon/seckillsession/getLates3DaysSession")
    public R getLates3DaysSession();
}
