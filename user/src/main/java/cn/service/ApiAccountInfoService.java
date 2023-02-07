package cn.service;

import cn.entity.ApiAccountInfo;
import main.java.cn.common.BackResult;
import main.java.cn.domain.ApiAccountInfoDomain;
import main.java.cn.service.ApiAccountInfoBusService;

public interface ApiAccountInfoService extends ApiAccountInfoBusService{
	
	ApiAccountInfo findByCreUserIdAndName(Integer creUserId,String name);
	
	int save(ApiAccountInfo info);
	
	int update(ApiAccountInfo info);

	BackResult<ApiAccountInfoDomain> findByNameAndPwd(String name,String password);
}
