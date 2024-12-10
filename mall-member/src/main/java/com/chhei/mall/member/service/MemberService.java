package com.chhei.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chhei.common.utils.PageUtils;
import com.chhei.mall.member.entity.MemberEntity;
import com.chhei.mall.member.vo.MemberLoginVO;
import com.chhei.mall.member.vo.MemberRegisterVo;
import com.chhei.mall.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author chhei
 * @email 1835494827@qq.com
 * @date 2024-09-14 14:10:04
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void register(MemberRegisterVo vo);

	MemberEntity login(MemberLoginVO vo);

	MemberEntity login(SocialUser vo);
}

