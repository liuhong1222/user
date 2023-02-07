package cn.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SmsCodeMapper {
	
	int saveSmsCode(Map<String,Object> param);
}
