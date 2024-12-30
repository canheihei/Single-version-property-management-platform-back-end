package com.chhei.mall.feign;

import com.chhei.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("mall-product")
public interface ProductFeignService {

    @RequestMapping("/product/skuinfo/info/{skuId}")
    //@RequiresPermissions("product:skuinfo:info")
    public R info(@PathVariable("skuId") Long skuId);


    @GetMapping("/product/skuinfo/saleAttrs/{skuId}")
    public List<String> getSkuSaleAttrs(@PathVariable("skuId") Long skuId);
}
