package cn.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dao.AgentMapper;
import cn.entity.AgentCreUser;
import cn.service.AgentService;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.AgentApplyInfoDomain;
import main.java.cn.domain.AgentCreUserDomain;
import main.java.cn.domain.AgentWebSiteDomain;
import main.java.cn.domain.WebSiteInfoDomain;

@Service
public class AgentServiceImpl implements AgentService {

	private final static Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

	@Autowired
	private AgentMapper agentMapper;

	@Override
	public BackResult<AgentWebSiteDomain> getAgentIdByDomain(String domain) {
		BackResult<AgentWebSiteDomain> result = new BackResult<AgentWebSiteDomain>();
		AgentWebSiteDomain agentId = agentMapper.getAgentIdByDomain(domain);
		if(agentId == null){
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("该域名不存在");
			return result;
		}
		
		result.setResultObj(agentId);
		return result;
	}

	@Override
	public BackResult<Integer> saveAgentCreUser(AgentCreUserDomain agentCreUserDomain) {
		BackResult<Integer> result = new BackResult<Integer>();
		int counts = agentMapper.saveAgentCreUser(agentCreUserDomain);
		if(counts != 1){
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("数据入库异常");
			return result;
		}
		
		result.setResultObj(counts);
		return result;
	}

	@Override
	public BackResult<String> getAgentIdByCreUserId(String creUserId) {
		BackResult<String> result = new BackResult<String>();
		AgentCreUser agentCreUser = agentMapper.getAgentCreUser(creUserId);
		if(agentCreUser == null){
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("用户不存在");
			return result;
		}
		
		result.setResultObj(agentCreUser.getAgentId().toString());
		return result;
	}

	@Override
	public BackResult<AgentWebSiteDomain> getAgentInfoByCreUserId(String creUserId) {
		BackResult<AgentWebSiteDomain> result = new BackResult<AgentWebSiteDomain>();
		AgentWebSiteDomain agentInfo = agentMapper.getAgentInfoByCreUserId(creUserId);
		if(agentInfo == null){
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("该域名不存在");
			return result;
		}
		
		result.setResultObj(agentInfo);
		return result;
	}

	@Override
	public BackResult<Map<String, Object>> getAgentPackage(Map<String, String> param) {
		BackResult<Map<String, Object>> result = new BackResult<Map<String, Object>>();
		Map<String, Object> agentPackage = agentMapper.getAgentPackage(param);
		if(agentPackage == null){
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("该套餐不存在");
			return result;
		}
		
		result.setResultObj(agentPackage);
		return result;
	}

	@Override
	public BackResult<WebSiteInfoDomain> getWebSiteInfo(String agentId) {
		BackResult<WebSiteInfoDomain> result = new BackResult<WebSiteInfoDomain>();
		WebSiteInfoDomain webSiteInfo = agentMapper.getWebSiteInfo(agentId);
		if(webSiteInfo == null){
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("网站信息不存在");
			return result;
		}
		
		result.setResultObj(webSiteInfo);
		return result;
	}

	@Override
	public BackResult<String> getAgentPayInfo(String agentId) {
		BackResult<String> result = new BackResult<String>();
		String isPay = "0";
		long counts  = agentMapper.getAgentPayInfo(agentId);
		if(counts == 1){
			isPay = "1";
		}
		
		result.setResultObj(isPay);
		return result;
	}

	@Override
	public BackResult<String> getAgentBalanceByAgentId(String creUserId) {
		BackResult<String> result = new BackResult<String>();
		String agentBalance = agentMapper.getAgentBalanceByAgentId(creUserId);
		if(StringUtils.isBlank(agentBalance)){
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("获取支付信息失败");
			return result;
		}
		
		result.setResultObj(agentBalance);
		return result;
	}

	@Override
	public BackResult<String> agentApply(AgentApplyInfoDomain agentApplyInfoDomain) {
		BackResult<String> result = new BackResult<String>();
		int counts = agentMapper.saveAgentApplyInfo(agentApplyInfoDomain);
		if(counts != 1){
			result.setResultCode(ResultCode.RESULT_DATA_EXCEPTIONS);
			result.setResultMsg("数据入库失败");
			return result;
		}
		
		result.setResultObj("success");
		return result;
	}
}
