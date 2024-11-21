package com.chhei.mall.mallsearch.service.impl;

import com.chhei.mall.mallsearch.config.MallElasticSearchConfiguration;
import com.chhei.mall.mallsearch.constant.ESConstant;
import com.chhei.mall.mallsearch.service.MallSearchService;
import com.chhei.mall.mallsearch.vo.SearchParam;
import com.chhei.mall.mallsearch.vo.SearchResult;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchSortValues;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MallSearchServiceImpl implements MallSearchService {
	@Autowired
	private RestHighLevelClient client;

	@Override
	public SearchResult search(SearchParam param) {
		SearchResult result = null;
		// 1. 准备检索的请求
		SearchRequest request = buildSearchRequest(param);

		try {
			// 2.执行检索操作
			SearchResponse response = client.search(request, MallElasticSearchConfiguration.COMMON_OPTIONS);
			// 3.需要把检索的信息封装为SearchResult
			result = buildSearchResult(response,param);
		} catch (Exception e) {

		}
		return result;
	}

	private SearchRequest buildSearchRequest(SearchParam param){
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(ESConstant.PRODUCT_INDEX);
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		// 构建具体的检索的条件
		// 1.构建bool查询
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		// 1.1 关键字的条件
		if(!StringUtils.isEmpty(param.getKeyword())){
			boolQuery.must(QueryBuilders.matchQuery("subTitle",param.getKeyword()));
		}
		// 1.2 类别的检索条件
		if(param.getCatalog3Id() != null){
			boolQuery.filter(QueryBuilders.termQuery("catalogId",param.getCatalog3Id()));
		}
		// 1.3 品牌的检索条件
		if(param.getBrandId() != null && param.getBrandId().size() > 0){
			boolQuery.filter(QueryBuilders.termsQuery("brandId",param.getBrandId()));
		}
		// 1.4 是否有库存
		if(param.getHasStock() != null){
			boolQuery.filter(QueryBuilders.termQuery("hasStock",param.getHasStock() == 1));
		}
		// 1.5 根据价格区间来检索
		if(!StringUtils.isEmpty(param.getSkuPrice())){
			String[] msg = param.getSkuPrice().split("_");
			RangeQueryBuilder skuPrice = QueryBuilders.rangeQuery("skuPrice");
			if(msg.length == 2){
				// 说明是 200_300这种格式
				skuPrice.gte(msg[0]);
				skuPrice.lte(msg[1]);
			}else if(msg.length == 1){
				// 说明是 _300  200_这种格式
				if(param.getSkuPrice().endsWith("_")){
					// 说明是 200_这种格式
					skuPrice.gte(msg[0]);
				}
				if(param.getSkuPrice().startsWith("_")){
					// 说明是 _300这种格式
					skuPrice.lte(msg[0]);
				}
			}
			boolQuery.filter(skuPrice);
		}
		// 1.6 属性的检索条件 attrs=20_8英寸:10英寸&attrs=19_64GB:32GB
		if(param.getAttrs() != null && param.getAttrs().size() > 0){
			for (String attrStr : param.getAttrs()) {
				BoolQueryBuilder boolNestedQuery = QueryBuilders.boolQuery();
				// attrs=19_64GB:32GB 我们首先需要根据 _ 做分割
				String[] attrStrArray = attrStr.split("_");
				// 属性的编号
				String attrId = attrStrArray[0];
				// 64GB:32GB  获取属性的值
				String[] values = attrStrArray[1].split(":");
				// 拼接组合条件
				boolNestedQuery.must(QueryBuilders.termQuery("attrs.attrId",attrId));
				boolNestedQuery.must(QueryBuilders.termsQuery("attrs.attrValue",values));

				NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", boolNestedQuery, ScoreMode.None);
				boolQuery.filter(nestedQuery);
			}
		}
		sourceBuilder.query(boolQuery);

		// 2.排序
		if(!StringUtils.isEmpty(param.getSort())){
			// sort=salaCount_asc/desc
			String[] s = param.getSort().split("_");
			SortOrder order = s[1].equalsIgnoreCase("asc")? SortOrder.ASC:SortOrder.DESC;
			sourceBuilder.sort(s[0], order);
		}
		// 3.处理分页
		// Integer pageNum; // 页码
		if(param.getPageNum() != null){
			// 需要做分页处理 pageSize = 5
			// pageNum:1 from:0  [0,1,2,3,4]
			// pageNum:2 from:5 [5,6,7,8,9]
			// from = ( pageNum - 1 ) * pageSize
			sourceBuilder.from( (param.getPageNum() - 1 ) * ESConstant.PRODUCT_PAGESIZE);
			sourceBuilder.size(ESConstant.PRODUCT_PAGESIZE);
		}

		// 4. 设置高亮
		if(!StringUtils.isEmpty(param.getKeyword())){
			// 如果有根据关键字查询那么我们才需要高亮设置
			HighlightBuilder highlightBuilder = new HighlightBuilder();
			highlightBuilder.field("subTitle");
			highlightBuilder.preTags("<b style='color:red'>");
			highlightBuilder.postTags("</b>");
			sourceBuilder.highlighter(highlightBuilder);
		}

		// 5.聚合运算
		// 5.1 品牌的聚合
		TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
		brand_agg.field("brandId");
		brand_agg.size(50);
		// 品牌的子聚合
		brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(10));
		brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(10));
		sourceBuilder.aggregation(brand_agg);

		// 5.2 类别的聚合
		TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg");
		catalog_agg.field("catalogId");
		catalog_agg.size(10);
		// 类别的子聚合
		catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(10));
		sourceBuilder.aggregation(catalog_agg);

		// 5.3 属性的聚合
		NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
		// 属性id聚合
		TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg");
		attr_id_agg.field("attrs.attrId");
		attr_id_agg.size(10);
		// 属性id下的子聚合 属性名称和属性值
		attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(10));
		attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(10));
		attr_agg.subAggregation(attr_id_agg);
		sourceBuilder.aggregation(attr_agg);

		System.out.println("--->"+sourceBuilder.toString());
		searchRequest.source(sourceBuilder);
		return searchRequest;
	}

	private SearchResult buildSearchResult(SearchResponse response,SearchParam param){


		return null;
	}
}
