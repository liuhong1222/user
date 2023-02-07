package cn.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.entity.AccountInfo;
import cn.entity.ApiAccountInfo;
import main.java.cn.domain.AccountInfoDomain;
import main.java.cn.domain.ApiAccountInfoDomain;

@Mapper
public interface ApiAccountInfoMapper {

	List<ApiAccountInfo> findByCreUserIdAndName(Integer creUserId,String name);
	
	List<ApiAccountInfo> findByCreUserId(Integer creUserId);
	
	int save(ApiAccountInfo info);
	
	int update(ApiAccountInfo info);
	
	int updateDomain(ApiAccountInfoDomain info);
	
	List<ApiAccountInfo> findByNameAndPwd(String name, String password);
	
	AccountInfoDomain getAccountInfo(String name, String password);
	
	int updateTcAccount(Map<String,Object> param);
	
	int updateFcAccount(Map<String,Object> param);
	
	int updateMsAccount(Map<String,Object> param);
	
	String getResultPwd(String creUserId);
	
	ApiAccountInfo findByAppId(@Param("appId")String appId);
}
