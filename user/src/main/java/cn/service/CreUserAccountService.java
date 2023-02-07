package cn.service;

import cn.entity.CreUserAccount;
import main.java.cn.service.UserAccountBusService;

/**
 * CreUserService
 * 
 *
 */
public interface CreUserAccountService extends UserAccountBusService{

	CreUserAccount findCreUserAccountByUserId(Integer creUserId);

	int saveCreUserAccount(CreUserAccount creUserAccount);

	int updateCreUserAccount(CreUserAccount creUserAccount);
	
	int updateApiAccount(CreUserAccount creUserAccount);
}
