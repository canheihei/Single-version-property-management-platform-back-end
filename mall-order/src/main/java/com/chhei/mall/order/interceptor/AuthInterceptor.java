package com.chhei.mall.order.interceptor;

import com.chhei.common.constant.AuthConstant;
import com.chhei.common.vo.MemberVO;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor {

    public static ThreadLocal threadLocal = new ThreadLocal();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 支付宝的回调我们放过
        String requestURI = request.getRequestURI();

        boolean match = new AntPathMatcher().match("/orderPay/**", requestURI);
        if(match){
            return true;
        }
        // 通过HttpSession获取当前登录的用户信息
        HttpSession session = request.getSession(false);
        Object attribute = session.getAttribute(AuthConstant.AUTH_SESSION_REDIS);
        if(attribute != null){
            MemberVO memberVO = (MemberVO) attribute;
            threadLocal.set(memberVO);
            return true;
        }
        // 如果 attribute == null 说明没有登录，那么我们就需要重定向到登录页面
        session.setAttribute(AuthConstant.AUTH_SESSION_MSG,"请先登录");
        response.sendRedirect("http://auth.chhei.com/login.html");
        return false;
    }
}
