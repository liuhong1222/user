package cn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.entity.AgentCreUser;
import cn.service.AgentService;
import cn.service.CosumptionLogService;
import main.java.cn.common.BackResult;
import main.java.cn.domain.AgentCreUserDomain;
import main.java.cn.domain.AgentWebSiteDomain;
import main.java.cn.domain.NumberCheckLogDomain;
import main.java.cn.domain.WaterConsumptionDomain;

@RestController
@RequestMapping("/consumptionLog")
public class CosumptionLogController{

	@Autowired
	private CosumptionLogService cosumptionLogService;
	
	private final static Logger logger = LoggerFactory.getLogger(CosumptionLogController.class);
	
	/**
	 * 保存空号检测记录到mysql
	 * @return
	 */
	@RequestMapping("/saveNumberCheckConsumption")
	public BackResult<Integer> saveNumberCheckConsumption(HttpServletRequest request, HttpServletResponse response,String jsonStr) {
		JSONObject json = JSONObject.parseObject(jsonStr);
		NumberCheckLogDomain wcd = JSONObject.toJavaObject(json, NumberCheckLogDomain.class);
		return cosumptionLogService.saveNumberCheckConsumption(wcd);
	}
}
