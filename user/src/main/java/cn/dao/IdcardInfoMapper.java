package cn.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import main.java.cn.domain.IdCardInfoDomain;

@Mapper
public interface IdcardInfoMapper {
	
	Map<String,Object> getIdcardInfoByCreUserId(String creUserId);
	
	int saveIdcardInfo(Map<String,Object> param);
	
	int updateIdcardInfo(Map<String,Object> param);
	
	int updateIdcardInfoByDomain(IdCardInfoDomain idCardInfoDomain);
	
	Map<String,Object> getUserAuthInfo(Map<String,Object> param);
	
	Long getUserAuthCount(String idno);
}
