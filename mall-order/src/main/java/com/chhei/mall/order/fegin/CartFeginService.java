package com.chhei.mall.order.fegin;

import com.chhei.mall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("mall-cart")
public interface CartFeginService {
	@GetMapping("/getUserCartItems")
	public List<OrderItemVo> getUserCartItems();
}
