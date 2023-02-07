package cn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.service.SmsCodeService;

@RestController
@RequestMapping("/smsCode")
public class SmsCodeController{

	@Autowired
	private SmsCodeService smsCodeService;
	
	private final static Logger logger = LoggerFactory.getLogger(SmsCodeController.class);
	
	/**
	 * 保存验证码发送记录
	 * @return
	 */
	@RequestMapping("/saveSmsCode")
	public void saveSmsCode(HttpServletRequest request, HttpServletResponse response,String mobile,String identifyCode) {		
		smsCodeService.saveSmsCode(mobile,identifyCode);
	}
}
