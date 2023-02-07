package cn.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.entity.AgentAccount;
import cn.entity.AgentAlipayInfo;
import cn.entity.AgentCreUser;
import cn.entity.AgentOrder;
import main.java.cn.domain.AgentApplyInfoDomain;
import main.java.cn.domain.AgentCreUserDomain;
import main.java.cn.domain.AgentWebSiteDomain;
import main.java.cn.domain.WebSiteInfoDomain;

@Mapper
public interface AgentMapper {
	
	AgentWebSiteDomain getAgentIdByDomain(String domain);
	
	AgentWebSiteDomain getAgentInfoByCreUserId(String creUserId);
	
	AgentCreUser getAgentCreUser(String creUserId);
	
	int saveAgentCreUser(AgentCreUserDomain agentCreUserDomain);
	
	Map<String,Object> getAgentContractInfos(String creUserId);
	
	AgentAlipayInfo getAgentAlipayInfos(String agentId);
	
	int saveAgentTrdOrder(Map<String,Object> param);
	
	Map<String,Object> getAgentPackage(Map<String,String> param);
	
	WebSiteInfoDomain getWebSiteInfo(String agentId);
	
	long getAgentPayInfo(String agentId);
	
	AgentAccount getAgentAccountByAgentId(String agentId);
	
	int updateAgentAccountByAgentId(Map<String,Object> param);
	
	int saveAgentOrder(AgentOrder agentOrder);
	
	String getAgentBalanceByAgentId(String creUserId);
		
	int saveAgentApplyInfo(AgentApplyInfoDomain agentApplyInfoDomain);
	
	int updateAgentCreUserByAgentId(@Param("creUserId") String creUserId,@Param("agentId") String agentId);
	
	int saveAgentChange(Map<String,Object> param);
}
