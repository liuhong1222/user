package cn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.entity.AgentCreUser;
import cn.service.AgentService;
import main.java.cn.common.BackResult;
import main.java.cn.domain.AgentApplyInfoDomain;
import main.java.cn.domain.AgentCreUserDomain;
import main.java.cn.domain.AgentWebSiteDomain;
import main.java.cn.domain.WebSiteInfoDomain;

@RestController
@RequestMapping("/agent")
public class AgentController{

	@Autowired
	private AgentService agentService;
	
	private final static Logger logger = LoggerFactory.getLogger(AgentController.class);
	
	/**
	 * 根据域名获取代理商id
	 * @return
	 */
	@RequestMapping("/getAgentIdByDomain")
	public BackResult<AgentWebSiteDomain> getAgentIdByDomain(HttpServletRequest request, HttpServletResponse response,String domain) {		
		return agentService.getAgentIdByDomain(domain);
	}
	
	/**
	 * 根据用户id获取代理商id
	 * @return
	 */
	@RequestMapping("/getAgentIdByCreUserId")
	public BackResult<String> getAgentIdByCreUserId(String creUserId) {		
		return agentService.getAgentIdByCreUserId(creUserId);
	}
	
	/**
	 * 根据用户id获取代理商域名和短信签名
	 * @return
	 */
	@RequestMapping("/getAgentInfoByCreUserId")
	public BackResult<AgentWebSiteDomain> getAgentInfoByCreUserId(String creUserId) {		
		return agentService.getAgentInfoByCreUserId(creUserId);
	}
	
	/**
	 * 保存用户-代理商映射表数据
	 * @return
	 */
	@RequestMapping("/saveAgentCreUser")
	public BackResult<Integer> saveAgentCreUser(HttpServletRequest request, HttpServletResponse response,AgentCreUserDomain agentCreUserDomain) {		
		return agentService.saveAgentCreUser(agentCreUserDomain);
	}
	
	/**
	 * 代理商系统初始化
	 * @return
	 */
	@RequestMapping("/getWebSiteInfo")
	public BackResult<WebSiteInfoDomain> getWebSiteInfo(HttpServletRequest request, HttpServletResponse response,String agentId) {		
		return agentService.getWebSiteInfo(agentId);
	}
	
	/**
	 * 代理商系统初始化
	 * @return
	 */
	@RequestMapping("/getAgentPayInfo")
	public BackResult<String> getAgentPayInfo(HttpServletRequest request, HttpServletResponse response,String agentId) {		
		return agentService.getAgentPayInfo(agentId);
	}
	
	/**
	 * 代理商系统初始化
	 * @return
	 */
	@RequestMapping("/agentApply")
	public BackResult<String> agentApply(HttpServletRequest request, HttpServletResponse response,@RequestBody AgentApplyInfoDomain agentApplyInfoDomain) {		
		return agentService.agentApply(agentApplyInfoDomain);
	}
}
