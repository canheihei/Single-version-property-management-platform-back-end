package com.chhei.mall.member.vo;

import lombok.Data;

/**
 * 注册用户的VO对象
 */
@Data
public class MemberRegisterVo {
    private String userName; // 账号

    private String password; // 密码

    private String phone;  // 手机号
}
