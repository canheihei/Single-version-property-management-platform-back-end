package com.chhei.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chhei.common.utils.PageUtils;
import com.chhei.mall.product.entity.BrandEntity;
import com.chhei.mall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-13 20:32:33
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

	PageUtils queryPage(Map<String, Object> params);

	void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

	void updateBrandName(Long brandId, String name);

	void updateCatelogName(Long catId, String name);

	List<CategoryBrandRelationEntity> categroyBrandRelation(Long catId);
}

