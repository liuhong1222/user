package cn.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import main.java.cn.domain.CreUserWarningDomain;

@Mapper
public interface CreUserWarningMapper {

	int saveCreUserWarning(Map<String,Object> param);

	int updateCreUserWarning(Map<String,Object> param);

	CreUserWarningDomain getCreUserWarning(Map<String,Object> param);
	
	List<CreUserWarningDomain> getCreUserWarningList();
}
