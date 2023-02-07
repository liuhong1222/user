package cn.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.AccountInfoDomain;
import main.java.cn.domain.ApiAccountInfoDomain;
import main.java.cn.service.ApiAccountInfoBusService;

@RestController
@RequestMapping("/apiAccountInfo")
public class ApiAccountInfoController {
	private final static Logger logger = LoggerFactory.getLogger(ApiAccountInfoController.class);
	
	@Autowired
	private ApiAccountInfoBusService apiAccountInfoBusService;
	
	@RequestMapping("/findByCreUserId")
	public BackResult<ApiAccountInfoDomain> findByCreUserId(String creUserId) {
		
		BackResult<ApiAccountInfoDomain> result = new BackResult<ApiAccountInfoDomain>();
		
		try {
			result = apiAccountInfoBusService.findByCreUserId(creUserId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户ID：" + creUserId + "查询api账户信息出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		} 
		
		return result;
	}
	
	@RequestMapping("/updateApiAccountInfo")
	public BackResult<ApiAccountInfoDomain> updateApiAccountInfo(@RequestBody ApiAccountInfoDomain domain) {
		
		BackResult<ApiAccountInfoDomain> result = new BackResult<ApiAccountInfoDomain>();
		
		try {
			result = apiAccountInfoBusService.updateApiAccountInfo(domain);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户ID：" + domain.getCreUserId() + "修改api账户信息出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		} 
		
		return result;
	}

	@RequestMapping("/findByNameAndPwd")
	public BackResult<ApiAccountInfoDomain> findByNameAndPwd(String name, String password) {
		
		BackResult<ApiAccountInfoDomain> result = new BackResult<ApiAccountInfoDomain>();
		
		try {
			result = apiAccountInfoBusService.findByNameAndPwd(name, password);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户ID：" + name + "查询api账户信息出现系统异常：" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		} 
		
		return result;
	}
	
	
	@RequestMapping("/checkApiAccount")
	public BackResult<Integer> checkApiAccount(String apiName, String password, String ip,
											   int checkCount){
		return apiAccountInfoBusService.checkApiAccount(apiName, password, ip, checkCount);
	}
	
	@RequestMapping("/checkMsAccount")
	public BackResult<Integer> checkMsAccount(String apiName, String password, String ip,int checkCount){
		return apiAccountInfoBusService.checkMsAccount(apiName, password, ip, checkCount);
	}
	
	@RequestMapping("/checkTcAccount")
	public BackResult<AccountInfoDomain> checkTcAccount(String apiName, String password, String method, String ip){
		return apiAccountInfoBusService.checkTcAccount(apiName, password,method, ip);
	}
	
	@RequestMapping("/checkTcAccountN")
	public BackResult<Integer> checkTcAccountN(String apiName, String password, String ip,int checkCount){
		return apiAccountInfoBusService.checkTcAccountN(apiName, password, ip,checkCount);
	}
	
	@RequestMapping("/checkFcAccountN")
	public BackResult<Integer> checkFcAccountN(String apiName, String password, String ip,int checkCount){
		return apiAccountInfoBusService.checkFcAccountN(apiName, password, ip,checkCount);
	}
	
	@RequestMapping("/checkCtAccountN")
	public BackResult<Integer> checkCtAccountN(String apiName, String password, String ip,int checkCount){
		return apiAccountInfoBusService.checkCtAccountN(apiName, password, ip,checkCount);
	}
	
	@RequestMapping("/updateTcAccount")
	public BackResult<Integer> updateTcAccount(@RequestBody Map<String,Object> params){
		return apiAccountInfoBusService.updateTcAccount(params);
	}

	@RequestMapping("/checkRqApiAccount")
	public BackResult<Integer> checkRqApiAccount(String apiName, String password, String ip,
											   int checkCount){
		return apiAccountInfoBusService.checkRqApiAccount(apiName, password, ip, checkCount);
	}
	
	@RequestMapping("/checkFiAccount")
	public BackResult<Integer> checkFiAccount(String apiName, String password, String ip,int checkCount){
		return apiAccountInfoBusService.checkFiAccount(apiName, password, ip,checkCount);
	}
	
	@RequestMapping("/checkFfAccount")
	public BackResult<Integer> checkFfAccount(String apiName, String password, String ip,int checkCount){
		return apiAccountInfoBusService.checkFfAccount(apiName, password, ip,checkCount);
	}
	
	@RequestMapping("/checkClAccount")
	public BackResult<Integer> checkClAccount(String apiName, String password, String ip,int checkCount){
		return apiAccountInfoBusService.checkClAccount(apiName, password, ip,checkCount);
	}
	
	@RequestMapping("/checkIdocrAccount")
	public BackResult<Integer> checkIdocrAccount(String apiName, String password, String ip,int checkCount){
		return apiAccountInfoBusService.checkIdocrAccount(apiName, password, ip,checkCount);
	}
	
	@RequestMapping("/checkBlocrAccount")
	public BackResult<Integer> checkBlocrAccount(String apiName, String password, String ip,int checkCount){
		return apiAccountInfoBusService.checkBlocrAccount(apiName, password, ip,checkCount);
	}
	
	@RequestMapping("/checkBocrAccount")
	public BackResult<Integer> checkBocrAccount(String apiName, String password, String ip,int checkCount){
		return apiAccountInfoBusService.checkBocrAccount(apiName, password, ip,checkCount);
	}
	
	@RequestMapping("/checkDocrAccount")
	public BackResult<Integer> checkDocrAccount(String apiName, String password, String ip,int checkCount){
		return apiAccountInfoBusService.checkDocrAccount(apiName, password, ip,checkCount);
	}
	
	@RequestMapping("/findByAppId")
	public BackResult<ApiAccountInfoDomain> findByAppId(String appId) {		
		return apiAccountInfoBusService.findByAppId(appId);
	}
}
