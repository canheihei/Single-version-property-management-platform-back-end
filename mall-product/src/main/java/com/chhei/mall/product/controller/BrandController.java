package com.chhei.mall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.chhei.common.valid.groups.AddGroupsInterface;
import com.chhei.common.valid.groups.UpdateGroupsInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.chhei.mall.product.entity.BrandEntity;
import com.chhei.mall.product.service.BrandService;
import com.chhei.common.utils.PageUtils;
import com.chhei.common.utils.R;


/**
 * 品牌
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-13 22:01:48
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("/all")
    public R queryAllBrand(){
        BrandEntity entity = new BrandEntity();
        entity.setName("华为");

        return R.ok().put("brand",entity);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Validated(AddGroupsInterface.class) @RequestBody BrandEntity brand){
        brandService.save(brand);

        return R.ok();
    }

    /*@RequestMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Valid @RequestBody BrandEntity brand, BindingResult result){
        if(result.hasErrors()){
            Map<String,String> map = new HashMap<>();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                String field = fieldError.getField();
                String defaultMessage = fieldError.getDefaultMessage();
                map.put(field,defaultMessage);
            }
            return R.error(400,"提交的品牌表单数据不合法").put("data",map);
        }
		brandService.save(brand);

        return R.ok();
    }*/

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@Validated(UpdateGroupsInterface.class) @RequestBody BrandEntity brand){
		brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
