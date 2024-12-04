package com.chhei.mall.member.exception;

public class UsernameExsitException extends RuntimeException{

    public UsernameExsitException(){
        super("账号已存在");
    }
}
