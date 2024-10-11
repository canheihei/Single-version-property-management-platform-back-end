package com.chhei.mall.product;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chhei.mall.product.entity.BrandEntity;
import com.chhei.mall.product.service.BrandService;
import com.chhei.mall.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = MallProductApplication.class)
class MallProductApplicationTests {
	@Autowired
	BrandService brandService;

	@Autowired
	private CategoryService categoryService;
	@Test
	void contextLoads() {
		BrandEntity entity = new BrandEntity();
		entity.setName("魅族");
		brandService.save(entity);
	}

	@Test
	void selectById(){
		List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id",2));
		for (BrandEntity entity : list) {
			System.out.println(entity);
		}
	}

	@Test
	public void test01(){
		Long[] catelogPath = categoryService.findCatelogPath(166l);
		for (Long aLong : catelogPath) {
			System.out.println(aLong);
		}
	}
}
