package cn.service;

import main.java.cn.common.BackResult;
import main.java.cn.domain.NumberCheckLogDomain;

/**
 * 空号检测记录
 *
 */
public interface CosumptionLogService{
			
	BackResult<Integer> saveNumberCheckConsumption(NumberCheckLogDomain numberCheckLogDomain);
}
