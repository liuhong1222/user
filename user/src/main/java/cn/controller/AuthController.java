package cn.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.service.UserAuthService;
import main.java.cn.common.BackResult;

@RestController
@RequestMapping("/userAuth")
public class AuthController {

	@Autowired
	private UserAuthService userAuthService;
	
	@RequestMapping("/saveBusAuthData")
	public BackResult<String> saveBusAuthData(@RequestBody Map<String,Object> param){
		return userAuthService.saveBusAuthData(param);
	}
	
	@RequestMapping("/saveIdcardAuthData")
	public BackResult<String> saveIdcardAuthData(@RequestBody Map<String,Object> param){
		return userAuthService.saveIdcardAuthData(param);
	}
	@RequestMapping("/isAuthByIdentyNo")
	public BackResult<Boolean> isAuthByIdentyNo(String identyType,String identyNo){
		return userAuthService.isAuthByIdentyNo(identyType,identyNo);
	}
}
