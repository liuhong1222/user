package cn.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dao.AgentMapper;
import cn.dao.BusinessLicenceInfoMapper;
import cn.dao.CreUserAccountMapper;
import cn.dao.CreUserMapper;
import cn.dao.CreUserWarningMapper;
import cn.dao.IdcardInfoMapper;
import cn.dao.InviteDetailMapper;
import cn.dao.TrdOrderMapper;
import cn.entity.AgentCreUser;
import cn.entity.CreUser;
import cn.entity.CreUserAccount;
import cn.entity.TrdOrder;
import cn.exception.ProcessException;
import cn.redis.RedisClient;
import cn.service.AgentService;
import cn.service.BaseLogService;
import cn.service.CreUserService;
import cn.utils.CommonUtils;
import cn.utils.DateUtils;
import main.java.cn.common.BackResult;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.AgentCreUserDomain;
import main.java.cn.domain.AgentWebSiteDomain;
import main.java.cn.domain.BusinessInfoDomain;
import main.java.cn.domain.CreUserAccountDomain;
import main.java.cn.domain.CreUserDomain;
import main.java.cn.domain.CreUserWarningDomain;
import main.java.cn.domain.IdCardInfoDomain;
import main.java.cn.domain.InviteDetailDomain;

@Service
public class CreUserServiceImpl implements CreUserService {

	private final static Logger logger = LoggerFactory.getLogger(CreUserServiceImpl.class);

	@Autowired
	private CreUserMapper creUserMapper;
	
	@Autowired
	private CreUserAccountMapper creUserAccountMapper;
	
	@Autowired
	private CreUserWarningMapper creUserWarningMapper;
	
	@Autowired
	private AgentMapper agentMapper;
	
	@Autowired
	private BusinessLicenceInfoMapper businessLicenceInfoMapper;
	
	@Autowired
	private IdcardInfoMapper idcardInfoMapper;
	
	@Autowired
	private InviteDetailMapper inviteDetailMapper;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private TrdOrderMapper trdOrderMapper;

	@Autowired  
    protected RedisClient redisClinet;  
	
	@Autowired
	private BaseLogService baseLogService;

	@Override
	public CreUser findCreUserByUserPhone(String userPhone) {
		List<CreUser> list = creUserMapper.findCreUserByUserPhone(userPhone);
		return CommonUtils.isNotEmpty(list) ? null : list.get(0);
	}
	
	@Override
	public CreUser findCreUserByOpenId(String openId) {
		CreUser creUser = creUserMapper.findCreUserByOpenId(openId);
		return creUser==null ? null : creUser;
	}
	
	@Override
	public int saveCreUserByOpenId(CreUser creUser) {

		int row = 0;
		try {
			CreUser user = this.findCreUserByOpenId(creUser.getWxOpenid());

			if (null != user) {
				logger.error("???????????????" + creUser.getWxOpenid() + "???????????????????????????");
				return 1;
			}

			creUser.setCreateTime(new Date());
			creUser.setUpdateTime(new Date());
			creUserMapper.saveCreUser(creUser);
			
		} catch (Exception e) {
			logger.error("??????????????????" + creUser.getWxOpenid() + "??????????????????????????????????????????" + e.getMessage());
		} finally {
		}

		return row;
	}

	@Override
	public int saveCreUser(CreUser creUser) {

		int row = 0;
		
//		RedisLock lock = new RedisLock(redisTemplate, "userSave_" + creUser.getUserPhone(), 0, 0);

		try {
			CreUser user = this.findCreUserByUserPhone(creUser.getUserPhone());

			if (null != user) {
				logger.error("???????????????" + creUser.getUserPhone() + "???????????????????????????");
				return 1;
			}

			creUser.setCreateTime(new Date());
			creUser.setUpdateTime(new Date());
			creUserMapper.saveCreUser(creUser);
			
		} catch (Exception e) {
			logger.error("????????????????????????" + creUser.getUserPhone() + "??????????????????????????????????????????" + e.getMessage());
		} finally {
//			lock.unlock();
		}

		return row;
	}

	@Override
	public int updateCreUser(CreUser creUser) {
		return creUserMapper.updateCreUser(creUser);
	}
	
	@Override
	public int updateUserPhone(String userId, String phone) {
		Map<String,Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("phone", phone);
		//????????????????????????????????????
		int counts = creUserMapper.updateUserPhone(param);
		return counts;
	}

	@Override
	public BackResult<CreUserDomain> findbyMobile(String mobile,String ip) {

		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();

		try {
			CreUser user = this.findCreUserByUserPhone(mobile);

			if (null == user) {
				result.setResultCode(ResultCode.RESULT_DATA_EXCEPTIONS);
				result.setResultMsg("?????????????????????");
				return result;
			}

			CreUserDomain creUserDomain = new CreUserDomain();
			BeanUtils.copyProperties(user, creUserDomain);
			AgentCreUser agentCreUser = agentMapper.getAgentCreUser(user.getId().toString());
			if (agentCreUser == null) {
				result.setResultCode(ResultCode.RESULT_DATA_EXCEPTIONS);
				result.setResultMsg("??????????????????????????????");
				return result;
			}
			
			if(StringUtils.isNotBlank(ip)) {
				try {
					CreUser tempUser = new CreUser();
					tempUser.setId(user.getId());
					tempUser.setLastLoginIp(ip);
					tempUser.setLastLoginTime(new Date());
					this.updateCreUser(user);
				} catch (Exception e) {
				}
			}
			
			creUserDomain.setAgentId(agentCreUser.getAgentId());
			result.setResultObj(creUserDomain);
		} catch (Exception e) {
			logger.error("????????????????????????" + mobile + "????????????????????????????????????" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("??????????????????");
		}

		return result;
	}
	
	@Transactional
	public synchronized BackResult<CreUserDomain> findUserByOpenId(String openId) {
		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();

		CreUserDomain creUserDomains = new CreUserDomain();
		CreUser user = this.findCreUserByOpenId(openId);			
		if (user == null) {
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("????????????,???????????????");
			return result;
		} 			
		
		BeanUtils.copyProperties(user, creUserDomains);
		result.setResultObj(creUserDomains);
		return result;
	}
	
	@Override
	public BackResult<CreUserDomain> userRegister(Map<String, Object> param) {
		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();
		CreUserDomain creUserDomains = new CreUserDomain();
		CreUser user = this.findCreUserByUserPhone(param.get("userPhone").toString());
		if(user!=null){
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("????????????,????????????");					
			return result;
		}
		// ?????????????????????
		String register_key = "USER_IP_" + param.get("lastLoginIp").toString().replace(".", "");
		String count  = redisClinet.get(register_key);
		if (count == null || count.equals("") || count.equals("null")) {
			count = "0";
			redisClinet.set(register_key , "1",DateUtils.getMiao());
		}
		
		if (Integer.parseInt(redisClinet.get(register_key)) <= 2) {
			CreUser creUser = new CreUser();

			creUser.setUserPhone(param.get("userPhone").toString());
			creUser.setLastLoginIp(param.get("lastLoginIp").toString());
			creUser.setLastLoginTime(new Date());
			
			this.saveCreUser(creUser);
			creUser = this.findCreUserByUserPhone(param.get("userPhone").toString());
			BeanUtils.copyProperties(creUser, creUserDomains);
			//???????????????????????????????????????
			Boolean isWhiteUser = Boolean.parseBoolean(param.get("isWhiteUser").toString());
			// ??????5000 ???
			CreUserAccount creUserAccount = new CreUserAccount();
			creUserAccount.setAccount(isWhiteUser==true?5000:0); // ???????????????5000
			creUserAccount.setCreUserId(creUser.getId());
			creUserAccount.setApiAccount(30); // ??????api??????0???
			creUserAccount.setRqAccount(10);
			creUserAccount.setTcAccount(5);
			creUserAccount.setFcAccount(5);
			creUserAccount.setMsAccount(5);
			creUserAccount.setCtAccount(5);
			creUserAccount.setFiAccount(5);
			creUserAccount.setFfAccount(5);
			creUserAccount.setClAccount(5);
			creUserAccount.setIdocrAccount(5);
			creUserAccount.setBlocrAccount(5);
			creUserAccount.setBocrAccount(5);
			creUserAccount.setDocrAccount(5);
			creUserAccount.setVersion(0);
			creUserAccount.setCreateTime(new Date());
			creUserAccount.setUpdateTime(new Date());
			creUserAccount.setDeleteStatus("0");
			creUserAccountMapper.saveCreUserAccount(creUserAccount);
			//????????????????????????
			TrdOrder trdOrder = new TrdOrder();
			trdOrder.setCreUserId(creUser.getId());
			trdOrder.setOrderNo("CLSH_"+String.valueOf(System.currentTimeMillis())+(new Random().nextInt(89)+10));
			trdOrder.setProductsId(4);
			trdOrder.setNumber(5000);
			trdOrder.setMoney(BigDecimal.ZERO);
			trdOrder.setPayType("10");
			trdOrder.setPayTime(new Date());
			trdOrder.setType("1");
			trdOrder.setStatus("1");
			trdOrder.setDeleteStatus("0");
			trdOrder.setVersion(0);
			trdOrder.setCreateTime(new Date());
			trdOrder.setUpdateTime(new Date());
			trdOrderMapper.saveTrdOrder(trdOrder);
			//????????????-????????????????????????
			AgentCreUserDomain agentCreUserDomain = new AgentCreUserDomain();
			agentCreUserDomain.setAgentId(Integer.parseInt(param.get("agentId").toString()));
			agentCreUserDomain.setCreUserId(creUser.getId());
			agentCreUserDomain.setCreateTime(new Date());
			agentCreUserDomain.setUpdateTime(new Date());
			agentMapper.saveAgentCreUser(agentCreUserDomain);
			//??????????????????
			String userId = param.get("userId")==null?null:param.get("userId").toString();
			if(StringUtils.isNotBlank(userId)){
				InviteDetailDomain iid = inviteDetailMapper.getInviteDetailByMobile(creUser.getUserPhone());
				if(iid == null){
					CreUser  tempCreUser = creUserMapper.findById(Integer.parseInt(userId));
					if(tempCreUser != null){
						InviteDetailDomain tempIid = new InviteDetailDomain();
						tempIid.setCreUserId(Integer.parseInt(userId));
						tempIid.setMobile(creUser.getUserPhone());
						tempIid.setInviteType(2);
						tempIid.setFlag(1);
						tempIid.setCreatetime(new Date());
						tempIid.setUpdatetime(new Date());
						//??????????????????
						inviteDetailMapper.saveInviteDetail(tempIid);
					}							
				}
			}
			
			
			result.setResultObj(creUserDomains);					
			// ?????????????????????????????????
			int redisCount = Integer.parseInt(count) + 1;
			redisClinet.set(register_key, String.valueOf(redisCount),DateUtils.getMiao());
			//??????????????????????????????????????????redis
			redisClinet.set("ym:" + creUser.getId().toString() + "_YANGMAODANG", isWhiteUser.toString());
			System.out.println("ym:" + creUser.getId().toString() + "_YANGMAODANG");
		} else {
			result.setResultCode(ResultCode.RESULT_REGISTERFAILED);
			result.setResultMsg("IP????????????????????????");
		}
		return result;
	}
	
	@Transactional
	public synchronized BackResult<CreUserDomain> findOrsaveUser(Map<String,Object> param) {
		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();

		try {
			
			CreUserDomain creUserDomains = new CreUserDomain();
			CreUser user = this.findCreUserByUserPhone(param.get("userPhone").toString());
			
			String domain = param.get("domain").toString();
			if (null != user) {
				//????????????id???????????????id
				AgentCreUser agentCreUser = agentMapper.getAgentCreUser(user.getId().toString());
				if(agentCreUser == null){
					result.setResultCode(ResultCode.RESULT_FAILED);
					result.setResultMsg("????????????,??????????????????");					
					return result;
				}
				
				if(!"0".equals(agentCreUser.getAgentId().toString()) && !agentCreUser.getAgentId().toString().equals(param.get("agentId").toString())){
					//?????????????????????????????????
					AgentWebSiteDomain awsd = agentMapper.getAgentInfoByCreUserId(user.getId().toString());
					if(awsd == null){
						result.setResultCode(ResultCode.RESULT_FAILED);
						result.setResultMsg("????????????,?????????????????????");
						return result;
					}else{
						if ("khjc.com.cn,0904.cn".contains(domain)) {
							result.setResultCode(ResultCode.DOMAIN_ERROR);
							result.setResultMsg(awsd.getDomain().equals("data.253.com")?"https://"+awsd.getDomain():"http://"+awsd.getDomain());
							return result;
						}else {
							// ???????????????
							int count = agentMapper.updateAgentCreUserByAgentId(user.getId().toString(),param.get("agentId").toString());
							if (count < 1) {
								logger.error("{}, ???????????????90949????????????????????????????????????",user.getId());
								return BackResult.error("??????????????????????????????");
							}
							
							Map<String, Object> paramsMap = new HashMap<String, Object>();
							paramsMap.put("mobile", param.get("userPhone").toString());
							paramsMap.put("creUserId", user.getId().toString());
							paramsMap.put("outAgentId", awsd.getAgentId());
							paramsMap.put("inAgentId", param.get("agentId").toString());
							count = agentMapper.saveAgentChange(paramsMap);
							if (count < 1) {
								logger.error("{}, ??????????????????????????????????????????????????????",user.getId());
								throw new RuntimeException("??????????????????????????????");
							}
						}
					}
				}
				// ???????????????????????? ???????????????
				user.setLastLoginIp(param.get("lastLoginIp").toString());
				user.setLastLoginTime(new Date());
				this.updateCreUser(user);
				BeanUtils.copyProperties(user, creUserDomains);
				result.setResultObj(creUserDomains);
				return result;
			} else {
				result = handleCodeRegister(param,domain);
			}
			
		} catch (Exception e) {
			logger.error("????????????????????????" + param.get("userPhone").toString() + "???????????????????????????????????????, info:" + e);
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("??????????????????");
		}

		return result;
	}
	
	private BackResult<CreUserDomain> handleCodeRegister(Map<String,Object> param,String domain){
		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();
		CreUserDomain creUserDomains = new CreUserDomain();
		String register_key = "USER_IP_" + param.get("lastLoginIp").toString().replace(".", "");
		String count  = redisClinet.get(register_key);		
		if (count == null || count.equals("") || count.equals("null")) {
			count = "0";
			redisClinet.set(register_key , "1",DateUtils.getMiao());
		}
		
		if (Integer.parseInt(redisClinet.get(register_key)) <= 2) {
			AgentWebSiteDomain agentWebSiteDomain = agentMapper.getAgentIdByDomain(domain);
			if (agentWebSiteDomain == null) {
				result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
				result.setResultMsg("??????????????????");
				return result;
			}
			
			CreUser creUser = new CreUser();
			creUser.setUserPhone(param.get("userPhone").toString());
			creUser.setLastLoginIp(param.get("lastLoginIp").toString());
			creUser.setLastLoginTime(new Date());

			this.saveCreUser(creUser);
			creUser = this.findCreUserByUserPhone(param.get("userPhone").toString());
			BeanUtils.copyProperties(creUser, creUserDomains);
			//???????????????????????????????????????
			Boolean isWhiteUser = Boolean.parseBoolean(param.get("isWhiteUser").toString());
			// ??????5000 ???
			CreUserAccount creUserAccount = new CreUserAccount();
			creUserAccount.setAccount(isWhiteUser==true?5000:0); // ???????????????5000
			creUserAccount.setCreUserId(creUser.getId());
			creUserAccount.setApiAccount(30); // ??????api??????0???
			creUserAccount.setRqAccount(10);
			creUserAccount.setTcAccount(5);
			creUserAccount.setFcAccount(5);
			creUserAccount.setMsAccount(5);
			creUserAccount.setCtAccount(5);
			creUserAccount.setFiAccount(5);
			creUserAccount.setFfAccount(5);
			creUserAccount.setClAccount(5);
			creUserAccount.setIdocrAccount(5);
			creUserAccount.setBlocrAccount(5);
			creUserAccount.setBocrAccount(5);
			creUserAccount.setDocrAccount(5);
			creUserAccount.setVersion(0);
			creUserAccount.setCreateTime(new Date());
			creUserAccount.setUpdateTime(new Date());
			creUserAccount.setDeleteStatus("0");
			creUserAccountMapper.saveCreUserAccount(creUserAccount);
			if(isWhiteUser==true){
				//????????????????????????
				TrdOrder trdOrder = new TrdOrder();
				trdOrder.setCreUserId(creUser.getId());
				trdOrder.setOrderNo("CLSH_"+String.valueOf(System.currentTimeMillis())+(new Random().nextInt(89)+10));
				trdOrder.setProductsId(4);
				trdOrder.setNumber(5000);
				trdOrder.setMoney(BigDecimal.ZERO);
				trdOrder.setPayType("10");
				trdOrder.setPayTime(new Date());
				trdOrder.setType("1");
				trdOrder.setStatus("1");
				trdOrder.setDeleteStatus("0");
				trdOrder.setVersion(0);
				trdOrder.setCreateTime(new Date());
				trdOrder.setUpdateTime(new Date());
				trdOrderMapper.saveTrdOrder(trdOrder);
			}					
			//????????????-????????????????????????
			AgentCreUserDomain agentCreUserDomain = new AgentCreUserDomain();
			agentCreUserDomain.setAgentId(Integer.valueOf(agentWebSiteDomain.getAgentId()));
			agentCreUserDomain.setCreUserId(creUser.getId());
			agentCreUserDomain.setCreateTime(new Date());
			agentCreUserDomain.setUpdateTime(new Date());
			agentMapper.saveAgentCreUser(agentCreUserDomain);					
			result.setResultObj(creUserDomains);					
			// ?????????????????????????????????
			int redisCount = Integer.parseInt(count) + 1;
			redisClinet.set(register_key, String.valueOf(redisCount),DateUtils.getMiao());
			//??????????????????????????????????????????redis
			redisClinet.set("ym:" + creUser.getId().toString() + "_YANGMAODANG", isWhiteUser.toString());
		} else {
			result.setResultCode(ResultCode.RESULT_REGISTERFAILED);
			result.setResultMsg("IP????????????????????????");
		}
		
		return result;
	}

	@Transactional
	public BackResult<CreUserDomain> updateCreUser(CreUserDomain creUserDomain) {
		
		CreUser user = this.findCreUserByUserPhone(creUserDomain.getUserPhone());

		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();
		
		CreUserDomain creuserdomain = new CreUserDomain();
		
		try {
			if (null == user) {
				result.setResultCode(ResultCode.RESULT_DATA_EXCEPTIONS);
				result.setResultMsg("?????????????????????");
				return result;
			}
			
			user.setUserEmail(creUserDomain.getUserEmail());
			user.setUserType(creUserDomain.getUserType());
			if(StringUtils.isBlank(creUserDomain.getUserEmail())){
				user.setIsAuth(0);
			}else {
				int counts = creUserMapper.findByUserMail(creUserDomain.getUserEmail());
				if(counts > 0){
					result.setResultCode(ResultCode.RESULT_FAILED);
					result.setResultMsg("??????????????????????????????????????????");
					return result;
				}
			}
			
			int count = this.updateCreUser(user);
			if(count != 1){
				result.setResultCode(ResultCode.RESULT_FAILED);
				result.setResultMsg("??????????????????");
				return result;
			}
			
			BeanUtils.copyProperties(user, creuserdomain);
			
			result.setResultObj(creuserdomain);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("??????ID??????" + creUserDomain.getId() + "??????????????????????????????????????????" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("??????????????????");
		}
		
		return result;
	}

	@Override
	public BackResult<CreUserDomain> findById(Integer id) {
		
		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();
		
		try {
			CreUser user = creUserMapper.findById(id);
			
			if (null == user) {
				result.setResultMsg("?????????????????????????????????????????????????????????");
				result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
			} else {
				CreUserDomain domain = new CreUserDomain();
				BeanUtils.copyProperties(user, domain);
				result.setResultMsg("??????");
				result.setResultObj(domain);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("??????ID??????" + id + "??????????????????????????????" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("????????????");
		}
		return result;
	}

	@Transactional
	public BackResult<CreUserDomain> updateCreUser(String userPhone, String email) {
		CreUser user = this.findCreUserByUserPhone(userPhone);

		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();
		
		CreUserDomain creuserdomain = new CreUserDomain();
		
		try {
			if (null == user) {
				result.setResultCode(ResultCode.RESULT_DATA_EXCEPTIONS);
				result.setResultMsg("?????????????????????");
				return result;
			}
			
			user.setUserEmail(email);
			
			this.updateCreUser(user);
			
			BeanUtils.copyProperties(user, creuserdomain);
			
			result.setResultObj(creuserdomain);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("?????????????????????" + userPhone + "??????????????????????????????????????????" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("??????????????????");
		}
		
		return result;
	}

	@Transactional
	public BackResult<CreUserDomain> activateUser(CreUserDomain creUserDomain) {
		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();

		try {
			
			CreUserDomain creUserDomains = new CreUserDomain();

			CreUser user = this.findCreUserByUserPhone(creUserDomain.getUserPhone());

			if (null != user) {
				BeanUtils.copyProperties(user, creUserDomains);
				result.setResultObj(creUserDomains);
				result.setResultMsg("??????????????????");
				return result;
			} else {
				// ?????????????????????
				CreUser creUser = new CreUser();

				BeanUtils.copyProperties(creUserDomain, creUser);
				creUser.setLastLoginIp(creUserDomain.getLastLoginIp());
				creUser.setLastLoginTime(new Date());

				this.saveCreUser(creUser);
				creUser = this.findCreUserByUserPhone(creUserDomain.getUserPhone());
				BeanUtils.copyProperties(creUser, creUserDomains);
				
				// ??????5000 ???
				CreUserAccount creUserAccount = new CreUserAccount();
				creUserAccount.setAccount(5000); // ???????????????5000
				creUserAccount.setCreUserId(creUser.getId());
				creUserAccount.setApiAccount(30); // ??????api??????30???
				creUserAccount.setRqAccount(10); // ????????????????????????10???
				creUserAccount.setTcAccount(5);
				creUserAccount.setFcAccount(5);
				creUserAccount.setMsAccount(5);
				creUserAccount.setCtAccount(5);
				creUserAccount.setFiAccount(5);
				creUserAccount.setFfAccount(5);
				creUserAccount.setClAccount(5);
				creUserAccount.setIdocrAccount(5);
				creUserAccount.setBlocrAccount(5);
				creUserAccount.setBocrAccount(5);
				creUserAccount.setDocrAccount(5);
				creUserAccount.setVersion(0);
				creUserAccount.setCreateTime(new Date());
				creUserAccount.setUpdateTime(new Date());
				creUserAccount.setDeleteStatus("0");
				creUserAccountMapper.saveCreUserAccount(creUserAccount);
				//????????????-????????????????????????
				AgentCreUserDomain agentCreUserDomain = new AgentCreUserDomain();
				agentCreUserDomain.setAgentId(1);
				agentCreUserDomain.setCreUserId(creUser.getId());
				agentCreUserDomain.setCreateTime(new Date());
				agentCreUserDomain.setUpdateTime(new Date());
				agentMapper.saveAgentCreUser(agentCreUserDomain);
				
				result.setResultObj(creUserDomains);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("????????????????????????" + creUserDomain.getUserPhone() + "??????????????????????????????????????????" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("??????????????????");
		}

		return result;
	}

	@Override
	public BackResult<CreUserDomain> activateUserZzt(CreUserDomain creUserDomain) {
		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();

		try {
			
			CreUserDomain creUserDomains = new CreUserDomain();

			CreUser user = this.findCreUserByUserPhone(creUserDomain.getUserPhone());

			if (null != user) {
				BeanUtils.copyProperties(user, creUserDomains);
				result.setResultObj(creUserDomains);
				result.setResultMsg("??????????????????");
				return result;
			} else {
				// ?????????????????????
				CreUser creUser = new CreUser();

				BeanUtils.copyProperties(creUserDomain, creUser);
				creUser.setLastLoginIp(creUserDomain.getLastLoginIp());
				creUser.setLastLoginTime(new Date());
				this.saveCreUser(creUser);
				creUser = this.findCreUserByUserPhone(creUserDomain.getUserPhone());
				BeanUtils.copyProperties(creUser, creUserDomains);
				
				// ??????5000 ???
				CreUserAccount creUserAccount = new CreUserAccount();
				creUserAccount.setAccount(5000); // ???????????????5000
				creUserAccount.setCreUserId(creUser.getId());
				creUserAccount.setApiAccount(30); // ??????api??????0???
				creUserAccount.setRqAccount(10); // ????????????????????????10???
				creUserAccount.setTcAccount(5);
				creUserAccount.setFcAccount(5);
				creUserAccount.setMsAccount(5);
				creUserAccount.setCtAccount(5);
				creUserAccount.setFiAccount(5);
				creUserAccount.setFfAccount(5);
				creUserAccount.setClAccount(5);
				creUserAccount.setIdocrAccount(5);
				creUserAccount.setBlocrAccount(5);
				creUserAccount.setBocrAccount(5);
				creUserAccount.setDocrAccount(5);
				creUserAccount.setVersion(0);
				creUserAccount.setCreateTime(new Date());
				creUserAccount.setUpdateTime(new Date());
				creUserAccount.setDeleteStatus("0");
				creUserAccountMapper.saveCreUserAccount(creUserAccount);
				//????????????-????????????????????????
				AgentCreUserDomain agentCreUserDomain = new AgentCreUserDomain();
				agentCreUserDomain.setAgentId(1);
				agentCreUserDomain.setCreUserId(creUser.getId());
				agentCreUserDomain.setCreateTime(new Date());
				agentCreUserDomain.setUpdateTime(new Date());
				agentMapper.saveAgentCreUser(agentCreUserDomain);	
				//????????????????????????
				TrdOrder trdOrder = new TrdOrder();
				trdOrder.setCreUserId(creUser.getId());
				trdOrder.setOrderNo("CLSH_"+String.valueOf(System.currentTimeMillis())+(new Random().nextInt(89)+10));
				trdOrder.setProductsId(4);
				trdOrder.setNumber(5000);
				trdOrder.setMoney(BigDecimal.ZERO);
				trdOrder.setPayType("10");
				trdOrder.setPayTime(new Date());
				trdOrder.setType("1");
				trdOrder.setStatus("1");
				trdOrder.setDeleteStatus("0");
				trdOrder.setVersion(0);
				trdOrder.setCreateTime(new Date());
				trdOrder.setUpdateTime(new Date());
				trdOrderMapper.saveTrdOrder(trdOrder);
				result.setResultObj(creUserDomains);				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("????????????????????????" + creUserDomain.getUserPhone() + "??????????????????????????????????????????" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("??????????????????");
		}

		return result;
	}

	
	/**
	 * admin?????? user??????ip??????
	 */
	@Transactional
	public synchronized BackResult<CreUserDomain> findOrsaveTdsUser(CreUserDomain creUserDomain) {
		BackResult<CreUserDomain> result = new BackResult<CreUserDomain>();

		try {
			
			CreUserDomain creUserDomains = new CreUserDomain();

			CreUser user = this.findCreUserByUserPhone(creUserDomain.getUserPhone());

			if (null != user) {
				// ???????????????????????? ???????????????
				user.setLastLoginIp(creUserDomain.getLastLoginIp());
				user.setLastLoginTime(new Date());
				this.updateCreUser(user);
				BeanUtils.copyProperties(user, creUserDomains);
				result.setResultObj(creUserDomains);
				return result;
			} else {
				
					CreUser creUser = new CreUser();

					BeanUtils.copyProperties(creUserDomain, creUser);
					creUser.setLastLoginIp(creUserDomain.getLastLoginIp());
					creUser.setLastLoginTime(new Date());

					this.saveCreUser(creUser);
					creUser = this.findCreUserByUserPhone(creUserDomain.getUserPhone());
					BeanUtils.copyProperties(creUser, creUserDomains);
					
					// ??????5000 ???
					CreUserAccount creUserAccount = new CreUserAccount();
					creUserAccount.setAccount(5000); // ???????????????5000
					creUserAccount.setCreUserId(creUser.getId());
					creUserAccount.setApiAccount(30); // ??????api??????0???
					creUserAccount.setRqAccount(10);
					creUserAccount.setTcAccount(5);
					creUserAccount.setFcAccount(5);
					creUserAccount.setMsAccount(5);
					creUserAccount.setCtAccount(5);
					creUserAccount.setFiAccount(5);
					creUserAccount.setFfAccount(5);
					creUserAccount.setClAccount(5);
					creUserAccount.setIdocrAccount(5);
					creUserAccount.setBlocrAccount(5);
					creUserAccount.setBocrAccount(5);
					creUserAccount.setDocrAccount(5);
					creUserAccount.setVersion(0);
					creUserAccount.setCreateTime(new Date());
					creUserAccount.setUpdateTime(new Date());
					creUserAccount.setDeleteStatus("0");
					creUserAccountMapper.saveCreUserAccount(creUserAccount);
					
					result.setResultObj(creUserDomains);
		    	}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("????????????????????????" + creUserDomain.getUserPhone() + "??????????????????????????????????????????" + e.getMessage());
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("??????????????????");
		}
		return result;
	}

	@Override
	public BackResult<CreUserAccountDomain> getUserBalance(String apiName, String password) {
		BackResult<CreUserAccountDomain> result = new BackResult<CreUserAccountDomain>();
		Map<String,Object> param  = new HashMap<>();
		param.put("apiName", apiName);
		param.put("password", password);
		CreUserAccountDomain cad = creUserAccountMapper.getCreUserAccount(param);
		if(cad == null){
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("??????????????????");
			return result;
		}
		
		result.setResultObj(cad);
		return result;
	}

	@Override
	public BackResult<CreUserWarningDomain> getUserWarning(String userId, String productName) {
		BackResult<CreUserWarningDomain> result = new BackResult<CreUserWarningDomain>();
		Map<String,Object> param  = new HashMap<>();
		param.put("userId", userId);
		param.put("productName", productName);
		CreUserWarningDomain cad = creUserWarningMapper.getCreUserWarning(param);		
		result.setResultObj(cad);
		return result;
	}

	@Override
	public BackResult<String> updateUserWarning(String warningCount, String informMobiles, String userId,
			String productName) {
		BackResult<String> result = new BackResult<String>();
		Map<String,Object> param  = new HashMap<>();
		param.put("userId", userId);
		param.put("productName", productName);
		param.put("warningCount", warningCount);
		param.put("informMobiles", informMobiles);
		CreUserWarningDomain cad = creUserWarningMapper.getCreUserWarning(param);	
		if(cad == null){
			int count = creUserWarningMapper.saveCreUserWarning(param);
			if(count != 1){
				result.setResultCode(ResultCode.RESULT_FAILED);
				result.setResultMsg("???????????????");
				return result;
			}
		}else{
			int count = creUserWarningMapper.updateCreUserWarning(param);
			if(count != 1){
				result.setResultCode(ResultCode.RESULT_FAILED);
				result.setResultMsg("???????????????");
				return result;
			}
		}
		result.setResultObj("????????????");
		return result;
	}

	@Override
	public BackResult<List<Map<String,Object>>> getCreUserApiConsumeCounts(String userId, String productName, String month) {
		BackResult<List<Map<String,Object>>> result = new BackResult<List<Map<String,Object>>>();
	    String startDate = month + "-01";
	    String endDate = month + "-31";
	    Map<String,Object> param  = new HashMap<>();
		param.put("userId", userId);
		param.put("fieldName", getDBField(productName));
		param.put("productName", productName);
		param.put("startDate", startDate.replace("-", ""));
		param.put("endDate", endDate.replace("-", ""));
		//????????????
		List<Map<String,Object>> obj = creUserAccountMapper.getCreUserApiConsumeCounts(param);
		result.setResultObj(obj);
		return result;
	}
	
	@Transactional
	@Override
	public BackResult<String> subUserAuthByIdCard(IdCardInfoDomain idCardInfoDomain) {
		BackResult<String> result = new BackResult<String>();
		//??????????????????
		int count0 = idcardInfoMapper.updateIdcardInfoByDomain(idCardInfoDomain);
		if(count0 != 1){
			throw new ProcessException("????????????????????????????????????");
		}
		
		Map<String,Object> param = new HashMap<>();
		param.put("userId", idCardInfoDomain.getCreUserId());
		param.put("userType", "0");
		param.put("isAuth", "1");
		//??????????????????????????????????????????
		int count = creUserMapper.updateCreUserAuthStatus(param);
		if(count != 1){
			throw new ProcessException("????????????????????????????????????");
		}

//		int trdOrderCount = trdOrderMapper.getUserIsPresent(idCardInfoDomain.getCreUserId());
//		if(trdOrderCount <= 0){
//			//????????????????????????
//			TrdOrder trdOrder = new TrdOrder();
//			trdOrder.setCreUserId(Integer.parseInt(idCardInfoDomain.getCreUserId()));
//			trdOrder.setOrderNo("CLSH_"+String.valueOf(System.currentTimeMillis())+(new Random().nextInt(89)+10));
//			trdOrder.setProductsId(4);
//			trdOrder.setNumber(5000);
//			trdOrder.setMoney(BigDecimal.ZERO);
//			trdOrder.setPayType("10");
//			trdOrder.setPayTime(new Date());
//			trdOrder.setType("1");
//			trdOrder.setStatus("1");
//			trdOrder.setDeleteStatus("0");
//			trdOrder.setVersion(0);
//			trdOrder.setCreateTime(new Date());
//			trdOrder.setUpdateTime(new Date());
//			trdOrderMapper.saveTrdOrder(trdOrder);
//			//??????????????????
//			CreUserAccount creUserAccount = new CreUserAccount();
//			creUserAccount.setCreUserId(Integer.parseInt(idCardInfoDomain.getCreUserId()));
//			creUserAccount.setAccount(5000);
//			creUserAccount.setUpdateTime(new Date());
//			creUserAccountMapper.addByUserId(creUserAccount);
//		}
		result.setResultObj("success");
		return result;
	}
	
	@Transactional
	@Override
	public BackResult<String> subUserAuthByBusiness(BusinessInfoDomain businessInfoDomain) {
		BackResult<String> result = new BackResult<String>();
		//??????????????????
		int count0 = businessLicenceInfoMapper.updateBusinessInfoByDomain(businessInfoDomain);
		if(count0 != 1){
			throw new ProcessException("????????????????????????????????????");
		}
		
		Map<String,Object> param = new HashMap<>();
		param.put("userId", businessInfoDomain.getCreUserId());
		param.put("userType", "1");
		param.put("isAuth", "1");
		//??????????????????????????????????????????
		int count = creUserMapper.updateCreUserAuthStatus(param);
		if(count != 1){
			throw new ProcessException("????????????????????????????????????");
		}
		
		result.setResultObj("success");
		return result;
	}
	
	@Override
	public BackResult<Map<String,Object>> getUserAuthInfo(String userId, String userType) {
		BackResult<Map<String,Object>> result = new BackResult<Map<String,Object>>();
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("userType", userType);
		if("1".equals(userType)){
			resultMap = businessLicenceInfoMapper.getUserAuthInfo(param);
		}else{
			resultMap = idcardInfoMapper.getUserAuthInfo(param);
		}
		
		result.setResultObj(resultMap);
		return result;
	}
	
	@Override
	public BackResult<List<CreUserDomain>> getAllUserData(String mobile) {
		BackResult<List<CreUserDomain>> result = new BackResult<List<CreUserDomain>>();
		List<CreUserDomain> list = creUserMapper.getAllUserData(mobile);
		if(list == null || list.size() <= 0){
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("????????????");
			return result;
		}
		
		result.setResultObj(list);
		return result;
	}
	
	private String getDBField(String productName){
		String result = "";
		if("account".equals(productName)){
			result = "account";
		}else if("apiAccount".equals(productName)){
			result = "api_account";
		}else if("rqAccount".equals(productName)){
			result = "rq_account";
		}else if("tcAccount".equals(productName)){
			result = "tc_account";
		}else if("fcAccount".equals(productName)){
			result = "fc_account";
		}else if("msAccount".equals(productName)){
			result = "ms_account";
		}else if("ctAccount".equals(productName)){
			result = "ct_account";
		}else if("fiAccount".equals(productName)){
			result = "fi_account";
		}else if("ffAccount".equals(productName)){
			result = "ff_account";
		}else if("clAccount".equals(productName)){
			result = "cl_account";
		}else if("idocrAccount".equals(productName)){
			result = "idocr_account";
		}else if("blocrAccount".equals(productName)){
			result = "blocr_account";
		}else if("bocrAccount".equals(productName)){
			result = "bocr_account";
		}else if("docrAccount".equals(productName)){
			result = "docr_account";
		}
		return result;
	}
}
