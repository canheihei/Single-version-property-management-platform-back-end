package com.chhei.mall.Interceptor;

import com.chhei.common.constant.AuthConstant;
import com.chhei.common.vo.MemberVO;
import org.bouncycastle.operator.AADProcessor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor {
	//本地线程对象
	public static ThreadLocal<MemberVO> threadLocal = new ThreadLocal();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		Object attribute = session.getAttribute(AuthConstant.AUTH_SESSION_REDIS);
		if (null != attribute) {
			MemberVO memberVO = (MemberVO) attribute;
			threadLocal.set(memberVO);
			return true;
		}
		session.setAttribute(AuthConstant.AUTH_SESSION_MSG,"请先登录");
		response.sendRedirect("http://auth.chhei.com/login.html");
		return false;
	}
}
