package com.chhei.mall.controller;

import com.alibaba.fastjson.JSON;
import com.chhei.common.utils.HttpUtils;
import com.chhei.common.utils.R;
import com.chhei.common.vo.MemberVO;
import com.chhei.mall.feign.MemberFeginService;
import com.chhei.mall.vo.SocialUser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class OAuth2Controller {

    @Autowired
    private MemberFeginService memberFeginService;

    @RequestMapping("/oauth/weibo/success")
    public String weiboOAuth(@RequestParam("code") String code
                                ,HttpSession session
                                ,HttpServletResponse response) throws Exception {
        Map<String,String> body = new HashMap<>();
        body.put("client_id","4209324488");
        body.put("client_secret","2d213afbb2500596586892e90a76fe17");
        body.put("grant_type","authorization_code");
        body.put("redirect_uri","http://auth.chhei.com/oauth/weibo/success");
        body.put("code",code);
        // 根据Code获取对应的Token信息
        HttpResponse post = HttpUtils.doPost(
                "https://api.weibo.com"
                , "/oauth2/access_token"
                , "post"
                , new HashMap<>()
                , null
                , body
        );
        int statusCode = post.getStatusLine().getStatusCode();
        if(statusCode != 200){
            // 说明获取Token失败,就调回到登录页面
            return "redirect:http://auth.chhei.com/login.html";
        }
        // 说明获取Token信息成功
        String json = EntityUtils.toString(post.getEntity());
        SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
        R r = memberFeginService.socialLogin(socialUser);
        if(r.getCode() != 0){
            // 登录错误
            return "redirect:http://auth.chhei.com/login.html";
        }
        String entityJson = (String) r.get("entity");
        System.out.println("-------------------------->"+entityJson);
        MemberVO memberVO = JSON.parseObject(entityJson,MemberVO.class);
        session.setAttribute("loginUser",memberVO);
        // 注册成功就需要调整到商城的首页
        return "redirect:http://mall.chhei.com/home";
    }
}
