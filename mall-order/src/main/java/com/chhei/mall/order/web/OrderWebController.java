package com.chhei.mall.order.web;

import com.chhei.mall.order.service.OrderService;
import com.chhei.mall.order.vo.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderWebController {
	@Autowired
	private OrderService orderService;

	@GetMapping("/toTrade")
	public String toTrade(Model model) {
		OrderConfirmVo confirmVo =	orderService.confirmOrder();
		model.addAttribute("confirmVo", confirmVo);

		return "confirm";
	}
}
