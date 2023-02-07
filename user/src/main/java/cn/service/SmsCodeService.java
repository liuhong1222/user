package cn.service;

/**
 * 验证码
 *
 */
public interface SmsCodeService{
		
	void saveSmsCode(String mobile,String identifyCode);
}
