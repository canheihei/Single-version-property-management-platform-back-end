package com.chhei.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chhei.common.utils.PageUtils;
import com.chhei.mall.product.entity.AttrEntity;
import com.chhei.mall.product.vo.AttrGroupRelationVO;
import com.chhei.mall.product.vo.AttrResponseVo;
import com.chhei.mall.product.vo.AttrVO;

import java.util.List;
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

	PageUtils queryBasePage(Map<String, Object> params, Long catelogId, String attrType);

	AttrResponseVo getAttrInfo(Long attrId);

	void updateBaseAttr(AttrVO attr);

	void removeByIdsDetails(Long[] attrIds);

	List<AttrEntity> getRelationAttr(Long attrgroupId);

	void deleteRelation(AttrGroupRelationVO[] vos);

	PageUtils getNoAttrRelation(Map<String, Object> params, Long attrgroupId);
}

