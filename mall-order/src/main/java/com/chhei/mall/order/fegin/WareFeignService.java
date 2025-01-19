package com.chhei.mall.order.fegin;

import com.chhei.common.utils.R;
import com.chhei.mall.order.vo.WareSkuLockVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("mall-ware")
public interface WareFeignService {

    @PostMapping("/ware/waresku/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVO vo);
}
