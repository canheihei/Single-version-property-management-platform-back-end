package com.chhei.mall.member.exception;

/**
 * 手机号存储的自定义异常
 */
public class PhoneExsitExecption extends RuntimeException{

    public PhoneExsitExecption(){
        super("手机号已存在");
    }
}
