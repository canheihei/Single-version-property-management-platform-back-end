package com.chhei.mall.mallsearch.controller;

import com.chhei.mall.mallsearch.service.MallSearchService;
import com.chhei.mall.mallsearch.vo.SearchParam;
import com.chhei.mall.mallsearch.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {
	@Autowired
	private MallSearchService mallSearchService;

	@GetMapping("/list.html")
	public String listPage(SearchParam param){
		SearchResult search = mallSearchService.search(param);

		return "index";
	}
}
