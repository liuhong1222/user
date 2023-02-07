package cn.dao;

import org.apache.ibatis.annotations.Mapper;

import main.java.cn.domain.UserInviteInfoDomain;

@Mapper
public interface UserInviteInfoMapper {
	
	UserInviteInfoDomain getUserInviteInfoByCreUserId(String creUserId);
	
	int saveUserInviteInfo(UserInviteInfoDomain uiid);
	
	int UpdateUserInviteInfo(UserInviteInfoDomain uiid);

}
