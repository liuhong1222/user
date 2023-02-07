package cn.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dao.BusinessLicenceInfoMapper;
import cn.dao.CreUserMapper;
import cn.dao.IdcardInfoMapper;
import cn.exception.ProcessException;
import cn.redis.RedisClient;
import cn.service.UserAuthService;
import main.java.cn.common.BackResult;

@Service
public class UserAuthServiceImpl implements UserAuthService {

	private final static Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);

	@Autowired
	private BusinessLicenceInfoMapper businessLicenceInfoMapper;

	@Autowired
	private IdcardInfoMapper idcardInfoMapper;

	@Autowired
	private CreUserMapper creUserMapper;
	
	@Autowired
	private RedisClient redisClient;

	@Transactional
	@Override
	public BackResult<String> saveBusAuthData(Map<String, Object> param) {
		BackResult<String> result = new BackResult<String>();
		//获取用户是否已上传过照片
		Map<String,Object> authData = businessLicenceInfoMapper.getBusinessLicenceInfoByCreUserId(param.get("cre_user_id").toString());
		if(authData == null){
			int counts = businessLicenceInfoMapper.saveBusinessLicenceInfo(param);
			if(counts != 1){
				throw new ProcessException("操作失败，数据库异常");
			}
		}else{
			int counts = businessLicenceInfoMapper.updateBusinessLicenceInfo(param);
			if(counts != 1){
				throw new ProcessException("操作失败，数据库异常");
			}
		}
		
		result.setResultObj("success");
		return result;
	}

	@Override
	public BackResult<String> saveIdcardAuthData(Map<String, Object> param) {
		BackResult<String> result = new BackResult<String>();
		//获取用户是否已上传过照片
		Map<String,Object> authData = idcardInfoMapper.getIdcardInfoByCreUserId(param.get("cre_user_id").toString());
		if(authData == null){
			int counts = idcardInfoMapper.saveIdcardInfo(param);
			if(counts != 1){
				throw new ProcessException("操作失败，数据库异常");
			}
		}else{
			int counts = idcardInfoMapper.updateIdcardInfo(param);
			if(counts != 1){
				throw new ProcessException("操作失败，数据库异常");
			}
		}
		
		result.setResultObj("success");
		return result;
	}

	@Override
	public BackResult<Boolean> isAuthByIdentyNo(String identyType, String identyNo) {
		BackResult<Boolean> result = new BackResult<Boolean>();
		Long counts = null;
		if("business".equals(identyType)){
			counts = businessLicenceInfoMapper.getUserAuthCount(identyNo);
		}else{
			counts = idcardInfoMapper.getUserAuthCount(identyNo);
		}
		
		result.setResultObj(counts>=1?true:false);
		return result;
	}
}
