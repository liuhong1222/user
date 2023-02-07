package cn.service;

import java.util.Map;

import main.java.cn.common.BackResult;
import main.java.cn.domain.AgentApplyInfoDomain;
import main.java.cn.domain.AgentCreUserDomain;
import main.java.cn.domain.AgentWebSiteDomain;
import main.java.cn.domain.WebSiteInfoDomain;

/**
 * 代理商
 *
 */
public interface AgentService{
		
	BackResult<AgentWebSiteDomain> getAgentIdByDomain(String domain);
	
	BackResult<Integer> saveAgentCreUser(AgentCreUserDomain agentCreUserDomain);
	
	BackResult<String> getAgentIdByCreUserId(String creUserId);
	
	BackResult<AgentWebSiteDomain> getAgentInfoByCreUserId(String creUserId);
	
	BackResult<Map<String,Object>> getAgentPackage(Map<String,String> param);
	
	BackResult<WebSiteInfoDomain> getWebSiteInfo(String agentId);
	
	BackResult<String> getAgentPayInfo(String agentId);
	
	BackResult<String> getAgentBalanceByAgentId(String creUserId);
	
	BackResult<String> agentApply(AgentApplyInfoDomain agentApplyInfoDomain);
}
