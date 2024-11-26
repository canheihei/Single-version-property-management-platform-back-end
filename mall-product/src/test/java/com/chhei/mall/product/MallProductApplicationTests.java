package com.chhei.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chhei.mall.product.entity.BrandEntity;
import com.chhei.mall.product.service.AttrGroupService;
import com.chhei.mall.product.service.BrandService;
import com.chhei.mall.product.service.CategoryService;
import com.chhei.mall.product.service.SkuSaleAttrValueService;
import com.chhei.mall.product.vo.SkuItemSaleAttrVo;
import com.chhei.mall.product.vo.SpuItemGroupAttrVo;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = MallProductApplication.class)
class MallProductApplicationTests {
	@Autowired
	BrandService brandService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Autowired
	RedissonClient redissonClient;

	@Autowired
	AttrGroupService attrGroupService;

	@Autowired
	SkuSaleAttrValueService skuSaleAttrValueService;

	@Test
	public void testRedissonClient(){
		System.out.println("redissonClient:"+redissonClient);
	}

	//测试Redis连接
	@Test
	public void testStringRedisTemplate(){
		// 获取操作String类型的Options对象
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		// 插入数据
		ops.set("name","chhei"+ UUID.randomUUID());
		// 获取存储的信息
		System.out.println("刚刚保存的值："+ops.get("name"));
	}

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

	@Test
	public void test2(){
		List<SpuItemGroupAttrVo> attrgroupWithSpuId = attrGroupService.getAttrgroupWithSpuId(6l, 225l);
		for (SpuItemGroupAttrVo spuItemGroupAttrVo : attrgroupWithSpuId) {
			System.out.println(spuItemGroupAttrVo);
		}
	}

	@Test
	public void test3(){
		List<SkuItemSaleAttrVo> skuSaleAttrValueBySpuId = skuSaleAttrValueService.getSkuSaleAttrValueBySpuId(6l);
		for (SkuItemSaleAttrVo skuItemSaleAttrVo : skuSaleAttrValueBySpuId) {
			System.out.println(skuItemSaleAttrVo);
		}
	}
}
