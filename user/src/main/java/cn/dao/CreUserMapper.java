package cn.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import cn.entity.CreUser;
import main.java.cn.domain.CreUserDomain;

@Mapper
public interface CreUserMapper {

	CreUser findByUserId(Integer clAccountId);
	
	CreUser findById(Integer id);
	
	List<CreUser> findCreUserByUserPhone(String userPhone);
	
	CreUser findCreUserByOpenId(String openid);
	
	int saveCreUser(CreUser creUser);
	
	int updateCreUser(CreUser creUser);
	
	int updateCreUserAuthStatus(Map<String,Object> param);
	
	int updateUserPhone(Map<String,Object> param);
	
	int saveCreUserWxInfo(Map<String,Object> param);
	
	List<CreUserDomain> getAllUserData(String mobile);
	
	int findByUserMail(String userMail);
}
