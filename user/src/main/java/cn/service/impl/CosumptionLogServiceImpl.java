package cn.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dao.CosumptionLogMapper;
import cn.service.CosumptionLogService;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.NumberCheckLogDomain;
import main.java.cn.domain.WaterConsumptionDomain;

@Service
public class CosumptionLogServiceImpl implements CosumptionLogService {

	private final static Logger logger = LoggerFactory.getLogger(CosumptionLogServiceImpl.class);

	@Autowired
	private CosumptionLogMapper cosumptionLogMapper;

	@Override
	public BackResult<Integer> saveNumberCheckConsumption(NumberCheckLogDomain numberCheckLogDomain) {
		BackResult<Integer> result = new BackResult<Integer>();
		int counts = cosumptionLogMapper.saveNumberCheckConsumption(numberCheckLogDomain);
		if(counts != 1){
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("数据入库异常");
			return result;
		}
		
		result.setResultObj(counts);
		return result;
	}
	
}
