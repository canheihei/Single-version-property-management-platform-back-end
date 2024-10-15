package com.chhei.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chhei.common.utils.PageUtils;
import com.chhei.mall.product.entity.AttrEntity;
import com.chhei.mall.product.vo.AttrVO;

import java.util.Map;

/**
 * 商品属性
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-13 20:32:33
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void saveAttr(AttrVO vo);
}

