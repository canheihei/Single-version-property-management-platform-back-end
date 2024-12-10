package com.chhei.mall.feign;

import com.chhei.common.utils.R;
import com.chhei.mall.vo.LoginVo;
import com.chhei.mall.vo.SocialUser;
import com.chhei.mall.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 会员服务
 */
@FeignClient("mall-member")
public interface MemberFeginService {
    @PostMapping("/member/member/register")
    public R register(@RequestBody UserRegisterVo vo);

    @RequestMapping("/member/member/login")
    public R login(@RequestBody LoginVo vo);

    @RequestMapping("/member/member/oauth/login")
    public R socialLogin(@RequestBody SocialUser vo);
}
