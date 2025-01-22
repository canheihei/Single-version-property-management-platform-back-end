package com.chhei.mall.order.web;

import com.chhei.common.exception.NoStockExecption;
import com.chhei.mall.order.config.AlipayTemplate;
import com.chhei.mall.order.service.OrderService;
import com.chhei.mall.order.vo.OrderConfirmVo;
import com.chhei.mall.order.vo.OrderResponseVO;
import com.chhei.mall.order.vo.OrderSubmitVO;
import com.chhei.mall.order.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderWebController {
	@Autowired
	private OrderService orderService;

	@Autowired
	private AlipayTemplate alipayTemplate;

	@GetMapping("/toTrade")
	public String toTrade(Model model) {
		OrderConfirmVo confirmVo =	orderService.confirmOrder();
		model.addAttribute("confirmVo", confirmVo);

		return "confirm";
	}

	@PostMapping("/orderSubmit")
	public String orderSubmit(OrderSubmitVO vo, Model model, RedirectAttributes redirectAttributes) {
		Integer code = 0;
		OrderResponseVO responseVO = null;
		try {
			responseVO = orderService.submitOrder(vo);
			code = responseVO.getCode();
		} catch (NoStockExecption e) {
			code = 2;
		}

		if (code == 0) {
			if (responseVO == null || responseVO.getOrderEntity() == null || responseVO.getOrderEntity().getOrderSn() == null) {
				throw new IllegalArgumentException("Order response or its fields are null!");
			}
			model.addAttribute("orderResponseVO", responseVO);
			// 表示下单操作成功
			return "pay";
		} else {
			System.out.println("code=" + code);
			String msg = "订单失败";
			if (code == 1) {
				msg = msg + ":重复提交";
			} else if (code == 2) {
				msg = msg + ":锁定库存失败";
			}
			redirectAttributes.addFlashAttribute("msg", msg);
			// 表示下单操作失败
			return "redirect:http://order.chhei.com/toTrade";
		}
	}


	@GetMapping("/orderPay")
	public String orderPay(@RequestParam("orderSn") String orderSn){
		System.out.println("orderSn=" + orderSn);

		return "list";
	}

	@GetMapping(value = "/payOrder",produces = "text/html")
	@ResponseBody
	public String payOrder(@RequestParam("orderSn") String orderSn){
		PayVo payVo = orderService.getOrderPay(orderSn);
		String pay = alipayTemplate.pay(payVo);
		System.out.println(pay);

		return pay;
	}
}
