package com.chhei.mall.order.fegin;

import com.chhei.mall.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("mall-member")
public interface MemberFeignService {
	@GetMapping("/member/memberreceiveaddress/{memberId}/address")
	public List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);

}
