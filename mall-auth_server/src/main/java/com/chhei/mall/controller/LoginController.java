package com.chhei.mall.controller;

import com.alibaba.fastjson.JSON;
import com.chhei.common.constant.SMSConstant;
import com.chhei.common.exception.BizCodeEnume;
import com.chhei.common.utils.R;
import com.chhei.common.vo.MemberVO;
import com.chhei.mall.feign.MemberFeginService;
import com.chhei.mall.feign.ThirdPartFeginService;
import com.chhei.mall.vo.LoginVo;
import com.chhei.mall.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController {
	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private ThirdPartFeginService thirdPartFeginService;

	@Autowired
	MemberFeginService memberFeginService;

	@ResponseBody
	@GetMapping("/sms/sendCode")
	public R sendSmsCode(@RequestParam("phone") String phone){
		Object redisCode = redisTemplate.opsForValue().get(SMSConstant.SMS_CODE_PERFIX + phone);
		if(redisCode != null ){
			String s = redisCode.toString();
			if(!"".equals(s)){
				Long l = Long.parseLong(redisCode.toString().split("_")[1]);
				if (System.currentTimeMillis() - l <= 60000){
					// 说明验证码的发送间隔不足一分钟 提示
					return R.error(BizCodeEnume.VALID_SMS_EXCEPTION.getCode(),BizCodeEnume.VALID_SMS_EXCEPTION.getMsg());
				}
			}
		}
		String uuid = UUID.randomUUID().toString().replaceAll("[^0-9]", ""); // 提取 UUID 中的数字
		String code = uuid.substring(0, 6); // 截取前六位数字
		thirdPartFeginService.sendSmsCode(phone,code);
		code = code + "_"+System.currentTimeMillis();
		System.out.println(code);
		redisTemplate.opsForValue().set(SMSConstant.SMS_CODE_PERFIX+phone,code,10, TimeUnit.MINUTES);

		return R.ok();
	}

	@PostMapping("/sms/register")
	public String register(@Valid UserRegisterVo vo, BindingResult result, Model model){
		Map<String,String> map = new HashMap<>();
		if(result.hasErrors()){
			// 表示提交的数据不合法
			List<FieldError> fieldErrors = result.getFieldErrors();

			for (FieldError fieldError : fieldErrors) {
				String field = fieldError.getField();
				String defaultMessage = fieldError.getDefaultMessage();
				map.put(field,defaultMessage);
			}
			model.addAttribute("error",map);
			return "/reg";
		}else{
			// 验证码是否正确
			String code = (String)redisTemplate.opsForValue().get(SMSConstant.SMS_CODE_PERFIX + vo.getPhone());
			System.out.println(code);
			code = code.split("_")[0];
			System.out.println(code);
			if(!code.equals(vo.getCode())){
				// 说明验证码不正确
				map.put("code","验证码错误");
				model.addAttribute("error",map);
				return "/reg";
			}else{
				// 验证码正确  删除验证码
				redisTemplate.delete(SMSConstant.SMS_CODE_PERFIX + vo.getPhone());
				// 远程调用对应的服务 完成注册功能
				R r = memberFeginService.register(vo);
				if(r.getCode() == 0){
					// 注册成功
					return "redirect:http://auth.chhei.com/login.html";
				}else{
					// 注册失败
					map.put("msg",r.getCode()+":"+r.get("msg"));
					model.addAttribute("error",map);
					return "/reg";
				}
			}
		}
		//return "redirect:/login.html";
	}


	/**
	 * 注册的方法
	 * @return
	 */
	@PostMapping("/login")
	public String login(LoginVo loginVo ,
						HttpSession session){
		R r = memberFeginService.login(loginVo);
		if(r.getCode() == 0){
			String entityJson = (String) r.get("entity");
			MemberVO memberVO = JSON.parseObject(entityJson,MemberVO.class);
			session.setAttribute("loginUser",memberVO);
			System.out.println(memberVO);
			// 表示登录成功
			return "redirect:http://mall.chhei.com/home";
		}
		session.setAttribute("errors",r.get("msg"));

		// 表示登录失败,重新跳转到登录页面
		return "redirect:http://auth.chhei.com/login.html";
	}
}
