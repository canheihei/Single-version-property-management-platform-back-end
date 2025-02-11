package com.chhei.mall.product.fegin.fallback;

import com.chhei.common.exception.BizCodeEnume;
import com.chhei.common.utils.R;
import com.chhei.mall.product.fegin.SeckillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SeckillFeignServiceFallback implements SeckillFeignService {
    @Override
    public R getSeckillSessionBySkuId(Long skuId) {
        log.error("熔断降级....SeckillFeignService:{}",skuId);
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(),BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
}
