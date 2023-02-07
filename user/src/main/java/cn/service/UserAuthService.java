package cn.service;

import java.util.Map;

import main.java.cn.common.BackResult;

/**
 * 用户认证
 *
 */
public interface UserAuthService{
	
	BackResult<String> saveBusAuthData(Map<String,Object> param);

	BackResult<String> saveIdcardAuthData(Map<String,Object> param);
	
	BackResult<Boolean> isAuthByIdentyNo(String identyType,String identyNo);
}
