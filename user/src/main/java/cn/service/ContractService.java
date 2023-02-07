package cn.service;

import cn.entity.Contract;
import main.java.cn.common.BackResult;

/**
 * 合同
 *
 */
public interface ContractService{
	
	BackResult<String> getUserContractData(String userId,String userType,String orderNo);
	
	int saveContractData(Contract contract);
	
	int updateContractStatus(String userId,String orderNo);

}
