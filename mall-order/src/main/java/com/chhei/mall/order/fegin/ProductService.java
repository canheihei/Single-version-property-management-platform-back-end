package com.chhei.mall.order.fegin;

import com.chhei.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "mall-product")
public interface ProductService {
	@GetMapping("/product/brand/all")
	public R queryAllBrand();
}
