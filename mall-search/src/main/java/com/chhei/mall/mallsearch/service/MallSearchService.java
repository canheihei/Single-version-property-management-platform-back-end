package com.chhei.mall.mallsearch.service;

import com.chhei.mall.mallsearch.vo.SearchParam;
import com.chhei.mall.mallsearch.vo.SearchResult;

public interface MallSearchService {
	SearchResult search(SearchParam param);
}
