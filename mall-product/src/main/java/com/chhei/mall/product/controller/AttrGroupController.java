package com.chhei.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.chhei.mall.product.entity.AttrEntity;
import com.chhei.mall.product.service.AttrAttrgroupRelationService;
import com.chhei.mall.product.service.CategoryService;
import com.chhei.mall.product.service.impl.AttrServiceImpl;
import com.chhei.mall.product.vo.AttrGroupRelationVO;
import com.chhei.mall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chhei.mall.product.entity.AttrGroupEntity;
import com.chhei.mall.product.service.AttrGroupService;
import com.chhei.common.utils.PageUtils;
import com.chhei.common.utils.R;



/**
 * 属性分组
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-13 22:01:48
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrServiceImpl attrService;

    @Autowired
    private AttrAttrgroupRelationService relationService;

    // app/product/attrgroup/225/withattr?t=1641538390108
    @GetMapping("/{catelogId}/withattr")
    public R getAttrgroupWithAttrs(@PathVariable("catelogId") Long catelogId){
        // 根据三级分类的编号获取对应的属性组和属性组的属性信息
        List<AttrGroupWithAttrsVo> list = attrGroupService
                .getAttrgroupWithAttrsByCatelogId(catelogId);
        return R.ok().put("data",list);
    }

    //attr/relation/delete
    @PostMapping("/attr/relation/delete")
    public R relationDelete(@RequestBody AttrGroupRelationVO[] vos){
        attrService.deleteRelation(vos);
        return R.ok();
    }

    // attr/relation
    @PostMapping("/attr/relation")
    public R saveBatch(@RequestBody List<AttrGroupRelationVO> vos){
        relationService.saveBatch(vos);
        return R.ok();
    }

    // /6/noattr/relation?t=1641447927058&page=1&limit=10&key=
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId
            ,@RequestParam Map<String,Object> params){
        PageUtils pageUtils = attrService.getNoAttrRelation(params,attrgroupId);
        return R.ok().put("page",pageUtils);
    }

    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> list = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",list);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
        //PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] paths = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(paths);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
