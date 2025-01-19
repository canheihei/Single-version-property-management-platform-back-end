package com.chhei.mall.order.fegin;

import com.chhei.mall.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("mall-member")
public interface MemberFeignService {
	@GetMapping("/member/memberreceiveaddress/{memberId}/address")
	public List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);

	@RequestMapping("/member/memberreceiveaddress/getAddressById/{id}")
	//@RequiresPermissions("member:memberreceiveaddress:info")
	public MemberAddressVo getAddressById(@PathVariable("id") Long id);
}
