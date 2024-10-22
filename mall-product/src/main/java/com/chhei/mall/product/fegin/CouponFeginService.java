package com.chhei.mall.product.fegin;

import com.chhei.common.dto.SkuReductionDTO;
import com.chhei.common.dto.SpuBoundsDTO;
import com.chhei.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mall-coupon")
public interface CouponFeginService {

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveFullReductionInfo(@RequestBody SkuReductionDTO dto);

    @RequestMapping("/coupon/spubounds/saveSpuBounds")
    //@RequiresPermissions("coupon:spubounds:save")
    R saveSpuBounds(@RequestBody SpuBoundsDTO spuBounds);
}
