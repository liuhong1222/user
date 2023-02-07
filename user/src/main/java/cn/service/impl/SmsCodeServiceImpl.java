package cn.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.dao.SmsCodeMapper;
import cn.service.SmsCodeService;

@Service
public class SmsCodeServiceImpl implements SmsCodeService {

	private final static Logger logger = LoggerFactory.getLogger(SmsCodeServiceImpl.class);

	@Autowired
	private SmsCodeMapper smsCodeMapper;

	@Override
	public void saveSmsCode(String mobile, String identifyCode) {
		Map<String,Object> param = new HashMap<>();
		param.put("mobile", mobile);
		param.put("identifyCode", identifyCode);
		int counts = smsCodeMapper.saveSmsCode(param);
		logger.info("保存手机号码：" + mobile + " 的验证码记录" + (counts==1?"成功":"失败") + ", 验证码为：" + identifyCode);
	}
}
