package cn.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import main.java.cn.domain.InviteDetailDomain;

@Mapper
public interface InviteDetailMapper {
	
	InviteDetailDomain getInviteDetailByMobile(String mobile);

	int updateInviteDetail(InviteDetailDomain idd);
	
	int saveInviteDetail(InviteDetailDomain idd);
	
	List<Map<String,Object>> getInviteList(String creUserId);
}
