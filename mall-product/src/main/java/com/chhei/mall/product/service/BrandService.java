package com.chhei.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chhei.common.utils.PageUtils;
import com.chhei.mall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-13 20:32:33
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);
}

