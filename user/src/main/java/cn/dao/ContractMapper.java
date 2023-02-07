package cn.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import cn.entity.Contract;

@Mapper
public interface ContractMapper {

	long getUserMaxContractNo(Map<String,Object> param);
	
	Map<String,Object> getContractPartyAInfoNull(Map<String,Object> param);
	
	Map<String,Object> getContractPartyAInfo1(Map<String,Object> param);
	
	Map<String,Object> getContractPartyAInfo0(Map<String,Object> param);
	
	List<Map<String,Object>> getContractOrderList(Map<String,Object> param);
	
	int saveContractData(Contract contract);
	
	int updateContractStatus(Map<String,Object> param);
}
