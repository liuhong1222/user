package cn.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import cn.entity.CreUserAccount;
import main.java.cn.domain.CreUserAccountDomain;

@Mapper
public interface CreUserAccountMapper {

	List<CreUserAccount> findCreUserAccountByUserId(Integer creUserId);

	int saveCreUserAccount(CreUserAccount creUserAccount);

	int updateCreUserAccount(CreUserAccount creUserAccount);
	
	int updateApiAccount(CreUserAccount creUserAccount);

	int addByUserId(CreUserAccount creUserAccount);

	int subByUserId(CreUserAccount creUserAccount);

	CreUserAccountDomain getCreUserAccount(Map<String,Object> param);
	
	List<Map<String,Object>> getCreUserApiConsumeCounts(Map<String,Object> param);
}
