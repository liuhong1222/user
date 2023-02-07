package cn.service;

import cn.entity.CreUser;
import main.java.cn.service.UserBusService;

/**
 * CreUserService
 *
 */
public interface CreUserService extends UserBusService{
	
	CreUser findCreUserByUserPhone(String userPhone);
	
	
	CreUser findCreUserByOpenId(String openId);
	
	int saveCreUser(CreUser creUser);
	
	int saveCreUserByOpenId(CreUser creUser);
	
	int updateCreUser(CreUser creUser);
	
}
