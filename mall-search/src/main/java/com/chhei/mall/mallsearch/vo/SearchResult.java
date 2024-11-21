package com.chhei.mall.mallsearch.vo;

import com.chhei.common.dto.es.SkuESModel;
import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
	private List<SkuESModel> products;
	private Integer pageNum;
	private Long total;
	private List<BrandVO> brands;
	private List<AttrVo> attrs;
	private List<CatalogVO> catalogs;

	@Data
	public static class CatalogVO{
		private Long catalogId;
		private String catalogName;
	}


	@Data
	public static class BrandVO{
		private Long brandId;
		private String brandName;
		private String brandImg;
	}

	@Data
	public static class AttrVo{
		private Long attrId; // 属性的编号
		private String attrName; // 属性的名称
		private List<String> attrValue; // 属性的值
	}
}
