package cn.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import main.java.cn.domain.BusinessInfoDomain;

@Mapper
public interface BusinessLicenceInfoMapper {
	
	Map<String,Object> getBusinessLicenceInfoByCreUserId(String creUserId);
	
	int saveBusinessLicenceInfo(Map<String,Object> param);
	
	int updateBusinessLicenceInfo(Map<String,Object> param);
	
	int updateBusinessInfoByDomain(BusinessInfoDomain businessInfoDomain);
	
	Map<String,Object> getUserAuthInfo(Map<String,Object> param);
	
	Long getUserAuthCount(String regnum);
}
