package cn.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.dao.AgentMapper;
import cn.dao.BusinessLicenceInfoMapper;
import cn.dao.ContractMapper;
import cn.dao.CreUserMapper;
import cn.dao.IdcardInfoMapper;
import cn.entity.Contract;
import cn.exception.ProcessException;
import cn.redis.RedisClient;
import cn.service.ContractService;
import cn.service.UserAuthService;
import cn.utils.DateUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.until.RedisKeyUtil;

@Service
public class ContractServiceImpl implements ContractService {

	private final static Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

	@Autowired
	private ContractMapper contractMapper;
	
	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private AgentMapper agentMapper;

	@Override
	public BackResult<String> getUserContractData(String userId,String userType,String orderNo) {
		BackResult<String> result = new BackResult<String>();
		JSONObject json = new JSONObject();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("userId", userId);
		//合同签署日期
		data.put("curDate", DateUtils.getToday());
		//获取用户当天已经生成的合同数
		long contractCounts = contractMapper.getUserMaxContractNo(data);
		//合同编号,由当天日期+userId+当天流水号组成
		json.put("contractNo", getContractNo(userId,DateUtils.getToday(),contractCounts));
		//获取合同甲方信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("userType", userType);
		param.put("orderNo", orderNo);
		Map<String, Object> partyA = new HashMap<String, Object>();
		if(StringUtils.isBlank(userType)){
			partyA = contractMapper.getContractPartyAInfoNull(param);
		}
		if("1".equals(userType)){
			partyA = contractMapper.getContractPartyAInfo1(param);
		}
		if("0".equals(userType)){
			partyA = contractMapper.getContractPartyAInfo0(param);
		}
		if(partyA == null){
			logger.info("获取用户【" + userId + "】合同的甲方信息失败，数据为空");
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("客户信息获取失败");
			return result;
		}
		json.put("partyA", partyA);
		//获取用户合同的订单
		json.put("orderList", contractMapper.getContractOrderList(param));
		json.put("curDate", DateUtils.getToday());
		//获取代理商合同信息
		Map<String, Object> agentInfos = agentMapper.getAgentContractInfos(userId);
		String signPath = agentInfos.get("signPath").toString();
		String sealPath = agentInfos.get("sealPath").toString();
		String[] signPaths = signPath.split("/");
		String[] sealPaths = sealPath.split("/");
		agentInfos.put("signPath", signPaths[sealPaths.length-1]);
		agentInfos.put("sealPath", sealPaths[sealPaths.length-1]);
		json.put("agentInfos", agentInfos);
		String key = RedisKeyUtil.getContrasctDataKey(userId);
		redisClient.set(key, json.toJSONString());
		result.setResultObj(json.getString("contractNo"));
		return result;
	}
	
	@Override
	public int saveContractData(Contract contract) {
		return contractMapper.saveContractData(contract);
	}
	
	private String getContractNo(String userId,String curDate,long counts){
		String temp_center = "00000000";
		String centerStr = temp_center.substring(0, 8-userId.length()) + userId;
		String temp_last = "000";
		String nums = Long.toString(counts + 1);
		String lastStr = temp_last.substring(0, 3-nums.length()) + nums;
		return curDate.replace("-", "") + centerStr + lastStr;
		
	}

	@Override
	public int updateContractStatus(String userId, String orderNo) {
		Map<String,Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("orderNo", orderNo);
		return contractMapper.updateContractStatus(param);
	}
}
