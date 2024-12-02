package com.chhei.mall.third.controller;

import com.chhei.common.utils.R;
import com.chhei.mall.third.utils.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SMSController {
	@Autowired
	private SmsComponent smsComponent;

	@GetMapping("/sms/sendcode")
	public R sendSmsCode(@RequestParam("phone") String phone,@RequestParam("code") String code){
		smsComponent.sendSmsCode(phone,code);
		return R.ok();
	}
}
