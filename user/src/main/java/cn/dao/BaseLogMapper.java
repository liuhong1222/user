package cn.dao;

import org.apache.ibatis.annotations.Mapper;

import cn.entity.BaseLog;

@Mapper
public interface BaseLogMapper {
		
	int saveBaseLog(BaseLog baseLog);
	
}
