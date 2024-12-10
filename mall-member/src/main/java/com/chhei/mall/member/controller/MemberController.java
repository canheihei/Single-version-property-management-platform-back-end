package com.chhei.mall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.chhei.common.exception.BizCodeEnume;
import com.chhei.mall.member.exception.PhoneExsitExecption;
import com.chhei.mall.member.exception.UsernameExsitException;
import com.chhei.mall.member.vo.MemberLoginVO;
import com.chhei.mall.member.vo.MemberRegisterVo;
import com.chhei.mall.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chhei.mall.member.entity.MemberEntity;
import com.chhei.mall.member.service.MemberService;
import com.chhei.common.utils.PageUtils;
import com.chhei.common.utils.R;



/**
 * 会员
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-14 14:10:04
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public R register(@RequestBody MemberRegisterVo vo){
        try {
            memberService.register(vo);
        }catch (UsernameExsitException exception){
            return R.error(BizCodeEnume.USERNAME_EXSIT_EXCEPTION.getCode(),
                    BizCodeEnume.USERNAME_EXSIT_EXCEPTION.getMsg());
        }catch (PhoneExsitExecption exsitExecption) {
            return R.error(BizCodeEnume.PHONE_EXSIT_EXCEPTION.getCode(),
                    BizCodeEnume.PHONE_EXSIT_EXCEPTION.getMsg());
        }catch (Exception e){
            return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(),
                    BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
        }

        return R.ok();
    }

    @RequestMapping("/login")
    public R login(@RequestBody MemberLoginVO vo){
        MemberEntity entity = memberService.login(vo);
        if(entity != null){
            return R.ok().put("entity", JSON.toJSONString(entity));
        }

        return R.error(BizCodeEnume.USERNAME_PHONE_VALID_EXCEPTION.getCode(),
                BizCodeEnume.USERNAME_PHONE_VALID_EXCEPTION.getMsg());
    }

    @RequestMapping("/oauth/login")
    public R socialLogin(@RequestBody SocialUser vo){
        MemberEntity entity = memberService.login(vo);

        return R.ok().put("entity", JSON.toJSONString(entity));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
