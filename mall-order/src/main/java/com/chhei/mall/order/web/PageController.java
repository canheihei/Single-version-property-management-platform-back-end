package com.chhei.mall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

	@GetMapping("/{page}.html")
	public String goPage(@PathVariable("page") String page) {

		return page;
	}
}
