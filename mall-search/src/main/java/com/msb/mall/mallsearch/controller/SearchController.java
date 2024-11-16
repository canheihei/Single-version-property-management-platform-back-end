package com.msb.mall.mallsearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {
	@GetMapping("/list.html")
	public String listPage(Long catelog3Id){

		return "index";
	}

	@GetMapping("/search.html")
	public String searchPage(Long keyword){

		return "index";
	}
}
