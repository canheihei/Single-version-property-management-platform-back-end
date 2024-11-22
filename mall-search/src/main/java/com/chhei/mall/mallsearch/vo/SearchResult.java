package com.chhei.mall.mallsearch.vo;

import com.chhei.common.dto.es.SkuESModel;
import lombok.Data;
import java.util.List;

@Data
public class SearchResult {

	private List<SkuESModel> products; // 查询到的所有的商品信息 满足条件
	// 分页信息
	private Integer pageNum; // 当前页
	private Long total;  // 总的记录数
	private Integer totalPages; // 总页数
	private List<Integer> navs; // 需要显示的分页的页码

	// 当前查询的所有的商品涉及到的所有的品牌信息
	private List<BrandVO> brands;
	// 当前查询的所有的商品涉及到的所有的属性信息
	private List<AttrVo> attrs;

	// 当前查询的所有商品涉及到的所有的类别信息
	private List<CatalogVO> catalogs;


	@Data
	public static class CatalogVO{
		private Long catalogId;
		private String catalogName;
	}

	/**
	 * 品牌的相关信息
	 */
	@Data
	public static class BrandVO{
		private Long brandId; // 品牌的编号
		private String brandName; // 品牌的名称
		private String brandImg; // 品牌的图片
	}

	@Data
	public static class AttrVo{
		private Long attrId; // 属性的编号
		private String attrName; // 属性的名称
		private List<String> attrValue; // 属性的值
	}

}
