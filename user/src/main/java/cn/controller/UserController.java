package cn.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.entity.CreUser;
import cn.service.AgentService;
import cn.service.CreUserService;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.BusinessInfoDomain;
import main.java.cn.domain.CreUserAccountDomain;
import main.java.cn.domain.CreUserDomain;
import main.java.cn.domain.CreUserWarningDomain;
import main.java.cn.domain.IdCardInfoDomain;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private CreUserService creUserService;
	
	@Autowired
	private AgentService agentService;

	/**
	 * 查询用户信息
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/findbyMobile", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> findbyMobile(HttpServletRequest request, HttpServletResponse response,String mobile,String ip) {
		BackResult<CreUserDomain> result = creUserService.findbyMobile(mobile,ip);
		return result;
	}
	
	@RequestMapping(value = "/findOrsaveUser", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> saveUser(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,Object> param) {
		
		BackResult<CreUserDomain> result = creUserService.findOrsaveUser(param);
		return result;
	}
	
	@RequestMapping(value = "/userRegister", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> userRegister(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,Object> param) {
		
		BackResult<CreUserDomain> result = creUserService.userRegister(param);
		return result;
	}
	
	@RequestMapping(value = "/findUserByOpenId", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> findUserByOpenId(HttpServletRequest request, HttpServletResponse response,String openId) {
		
		BackResult<CreUserDomain> result = creUserService.findUserByOpenId(openId);
		return result;
	}
	
	@RequestMapping(value = "/updateCreUser", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> updateCreUser(HttpServletRequest request, HttpServletResponse response,@RequestBody CreUserDomain creUserDomain) {
		
		BackResult<CreUserDomain> result = creUserService.updateCreUser(creUserDomain);
		return result;
	}
	
	@RequestMapping(value = "/updateCreUserEmail", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> updateCreUser(HttpServletRequest request, HttpServletResponse response,String userPhone, String email) {
		
		BackResult<CreUserDomain> result = creUserService.updateCreUser(userPhone,email);
		return result;
	}
	
	@RequestMapping(value = "/findById", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> findById(HttpServletRequest request, HttpServletResponse response,Integer id) {
		
		BackResult<CreUserDomain> result = creUserService.findById(id);
		return result;
	}
	
	@RequestMapping(value = "/activateUser", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> activateUser(@RequestBody CreUserDomain creUserDomain){
		BackResult<CreUserDomain> result = creUserService.activateUser(creUserDomain);
		return result;
	}
	
	@RequestMapping(value = "/activateUserZzt", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserDomain> activateUserZzt(@RequestBody CreUserDomain creUserDomain){
		BackResult<CreUserDomain> result = creUserService.activateUserZzt(creUserDomain);
		return result;
	}
	
	@RequestMapping(value = "/getUserBalance", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserAccountDomain> getUserBalance(HttpServletRequest request, HttpServletResponse response,String apiName,String password){
		return creUserService.getUserBalance(apiName,password);
	}
	
	@RequestMapping(value = "/getUserWarning", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<CreUserWarningDomain> getUserWarning(HttpServletRequest request, HttpServletResponse response,String userId,String productName){
		return creUserService.getUserWarning(userId,productName);
	}
	
	@RequestMapping(value = "/updateUserWarning", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<String> updateUserWarning(HttpServletRequest request, HttpServletResponse response,String warningCount,String informMobiles,String userId,String productName){
		return creUserService.updateUserWarning(warningCount,informMobiles,userId,productName);
	}
	
	@RequestMapping(value = "/getCreUserApiConsumeCounts", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<Map<String,Object>>> getCreUserApiConsumeCounts(HttpServletRequest request, HttpServletResponse response,String userId,String productName,String month){
		return creUserService.getCreUserApiConsumeCounts(userId,productName,month);
	}
	
	@RequestMapping(value = "/subUserAuthByIdCard", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<String> subUserAuthByIdCard(@RequestBody IdCardInfoDomain idCardInfoDomain){
		return creUserService.subUserAuthByIdCard(idCardInfoDomain);
	}
	
	@RequestMapping(value = "/subUserAuthByBusiness", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<String> subUserAuthByBusiness(@RequestBody BusinessInfoDomain businessInfoDomain){
		return creUserService.subUserAuthByBusiness(businessInfoDomain);
	}
	
	@RequestMapping(value = "/getUserAuthInfo", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Map<String,Object>> getUserAuthInfo(HttpServletRequest request, HttpServletResponse response,String userId,String userType){
		return creUserService.getUserAuthInfo(userId,userType);
	}
	
	@RequestMapping(value = "/updateUserPhone", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<Integer> updateUserPhone(HttpServletRequest request, HttpServletResponse response,String userId,String phone){
		BackResult<Integer> result = new BackResult<Integer>();
		CreUser creUser  = creUserService.findCreUserByUserPhone(phone);
		if(creUser != null){
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("修改用户手机号码失败，输入的新手机号码已被注册");
			return result;
		}
		int counts = creUserService.updateUserPhone(userId,phone);
		if(counts !=1 ){
			result.setResultCode(ResultCode.RESULT_DATA_EXCEPTIONS);
			result.setResultMsg("修改用户手机号码错误，数据库异常");
			return result;
		}
		
		result.setResultObj(counts);
		return result;
	}
	
	@RequestMapping(value = "/getAllUserData", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public BackResult<List<CreUserDomain>> getAllUserData(String mobile){
		return creUserService.getAllUserData(mobile);
	}
}
