package com.chhei.mall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.chhei.common.dto.SkuHasStockDto;
import com.chhei.common.exception.BizCodeEnume;
import com.chhei.common.exception.NoStockExecption;
import com.chhei.mall.ware.vo.WareSkuLockVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chhei.mall.ware.entity.WareSkuEntity;
import com.chhei.mall.ware.service.WareSkuService;
import com.chhei.common.utils.PageUtils;
import com.chhei.common.utils.R;



/**
 * 商品库存
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-14 14:08:28
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    @PostMapping("/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVO vo){
        try {
            Boolean flag = wareSkuService.orderLockStock(vo);
        }catch (NoStockExecption e){
            // 表示锁定库存失败
            return R.error(BizCodeEnume.NO_STOCK_EXCEPTION.getCode(),BizCodeEnume.NO_STOCK_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    @PostMapping("/hasStock")
    public List<SkuHasStockDto> getSkusHasStock(@RequestBody List<Long> skuIds){
        List<SkuHasStockDto> list = wareSkuService.getSkusHasStock(skuIds);
        return list;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:waresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:waresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:waresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
