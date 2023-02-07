package cn.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dao.AgentMapper;
import cn.dao.ApiAccountInfoMapper;
import cn.dao.CreUserAccountMapper;
import cn.dao.CreUserMapper;
import cn.dao.TrdOrderMapper;
import cn.entity.ApiAccountInfo;
import cn.entity.CreUser;
import cn.entity.CreUserAccount;
import cn.entity.TrdOrder;
import cn.exception.ProcessException;
import cn.redis.RedisClient;
import cn.service.AgentService;
import cn.service.CreUserAccountService;
import cn.utils.CommonUtils;
import cn.utils.Constant;
import cn.utils.DateUtils;
import cn.utils.UUIDTool;
import main.java.cn.common.BackResult;
import main.java.cn.common.RedisKeys;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.ErpTradeDomain;
import main.java.cn.domain.TrdOrderDomain;
import main.java.cn.domain.UserAccountDomain;
import main.java.cn.domain.page.PageDomain;

@Service
public class CreUserAccountServiceImpl implements CreUserAccountService {

	private final static Logger logger = LoggerFactory.getLogger(CreUserAccountServiceImpl.class);

	@Autowired
	private CreUserAccountMapper creUserAccountMapper;

	@Autowired
	private CreUserMapper creUserMapper;

	@Autowired
	private TrdOrderMapper trdOrderMapper;

	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private AgentMapper agentMapper;
	
	@Autowired
	private ApiAccountInfoMapper apiAccountInfoMapper;
	
	@Override
	public CreUserAccount findCreUserAccountByUserId(Integer creUserId) {
		List<CreUserAccount> list = creUserAccountMapper.findCreUserAccountByUserId(creUserId);
		return CommonUtils.isNotEmpty(list) ? null : list.get(0);
	}

	@Override
	public int saveCreUserAccount(CreUserAccount creUserAccount) {
		return creUserAccountMapper.saveCreUserAccount(creUserAccount);
	}

	@Override
	public int updateCreUserAccount(CreUserAccount creUserAccount) {
		return creUserAccountMapper.updateCreUserAccount(creUserAccount);
	}
	
	@Override
	public int updateApiAccount(CreUserAccount creUserAccount) {
		return creUserAccountMapper.updateApiAccount(creUserAccount);
	}

	@Override
	public BackResult<UserAccountDomain> findByMobile(String mobile) {

		BackResult<UserAccountDomain> result = new BackResult<UserAccountDomain>();

		try {
			List<CreUser> user = creUserMapper.findCreUserByUserPhone(mobile);

			if (CommonUtils.isNotEmpty(user)) {
				result.setResultMsg("系统未查询到该用户");
				result.setResultCode(ResultCode.RESULT_FAILED);
				return result;
			}

			CreUserAccount account = this.findCreUserAccountByUserId(user.get(0).getId());

			if (null == account) {
				result.setResultMsg("系统未查询到该用户账户信息");
				result.setResultCode(ResultCode.RESULT_DATA_EXCEPTIONS);
				return result;
			}

			UserAccountDomain accountDomian = new UserAccountDomain();
			accountDomian.setAccount(account.getAccount());
			accountDomian.setCreUserId(user.get(0).getId());
			accountDomian.setApiAccount(account.getApiAccount());
			accountDomian.setRqAccount(account.getRqAccount());
			accountDomian.setTcAccount(account.getTcAccount());
			accountDomian.setFcAccount(account.getFcAccount());
			accountDomian.setMsAccount(account.getMsAccount());
			accountDomian.setCtAccount(account.getCtAccount());
			accountDomian.setFiAccount(account.getFiAccount());
			accountDomian.setFfAccount(account.getFfAccount());
			accountDomian.setClAccount(account.getClAccount());
			accountDomian.setIdocrAccount(account.getIdocrAccount());
			accountDomian.setBlocrAccount(account.getBlocrAccount());
			accountDomian.setBocrAccount(account.getBocrAccount());
			accountDomian.setDocrAccount(account.getDocrAccount());
			accountDomian.setIsFrozen(account.getIsFrozen());
			result.setResultObj(accountDomian);
		} catch (Exception e) {
			logger.error("用户手机号：【" + mobile + "】执行获取账户信息发生系统异常：" + e.getMessage());
			e.printStackTrace();
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}
		return result;
	}

	@Override
	public BackResult<UserAccountDomain> findByUserId(Integer creUserId) {
		BackResult<UserAccountDomain> result = new BackResult<UserAccountDomain>();
		CreUserAccount account = this.findCreUserAccountByUserId(creUserId);

		if (null == account) {
			result.setResultMsg("系统未查询到该用户账户信息");
			result.setResultCode(ResultCode.RESULT_DATA_EXCEPTIONS);
			return result;
		}

		UserAccountDomain accountDomian = new UserAccountDomain();
		accountDomian.setAccount(account.getAccount());
		accountDomian.setCreUserId(creUserId);
		accountDomian.setApiAccount(account.getApiAccount());
		accountDomian.setRqAccount(account.getRqAccount());
		accountDomian.setTcAccount(account.getTcAccount());
		accountDomian.setFcAccount(account.getFcAccount());
		accountDomian.setMsAccount(account.getMsAccount());
		accountDomian.setCtAccount(account.getCtAccount());
		accountDomian.setFiAccount(account.getFiAccount());
		accountDomian.setFfAccount(account.getFfAccount());
		accountDomian.setClAccount(account.getClAccount());
		accountDomian.setIdocrAccount(account.getIdocrAccount());
		accountDomian.setBlocrAccount(account.getBlocrAccount());
		accountDomian.setBocrAccount(account.getBocrAccount());
		accountDomian.setDocrAccount(account.getDocrAccount());
		accountDomian.setIsFrozen(account.getIsFrozen());
		result.setResultObj(accountDomian);
		return result;
	}
	
	@Transactional
	public synchronized BackResult<ErpTradeDomain> rechargeOrRefunds(TrdOrderDomain trdOrderDomain) {

		BackResult<ErpTradeDomain> result = new BackResult<ErpTradeDomain>();

		// 账号检测
		List<CreUser> userList = creUserMapper.findCreUserByUserPhone(trdOrderDomain.getMobile());

		if (CommonUtils.isNotEmpty(userList)) {
			result.setResultMsg("系统未查询到该用户");
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultObj(null);
			return result;
		}

		// 账户检测
		CreUserAccount creUserAccount = this.findCreUserAccountByUserId(userList.get(0).getId());

		if (null == creUserAccount) {
			result.setResultCode(ResultCode.RESULT_DATA_EXCEPTIONS);
			result.setResultMsg("数据库不存在该用户的账户记录");
			result.setResultObj(null);
			return result;
		}

		// 设置用户id 和订单号
		trdOrderDomain.setCreUserId(userList.get(0).getId());
		trdOrderDomain.setOrderNo("CLSH_" + System.currentTimeMillis());

		switch (trdOrderDomain.getType()) {
		case Constant.TRD_ORDER_TYPE_RECHARGE:
			result = this.recharge(trdOrderDomain, creUserAccount);
			break;
		case Constant.TRD_ORDER_TYPE_REFUNDS:
			result = this.refunds(trdOrderDomain, creUserAccount);
			break;
		default:
			result.setResultCode(ResultCode.RESULT_PARAM_EXCEPTIONS);
			result.setResultMsg(
					"用户ID为" + trdOrderDomain.getCreUserId() + "无法解析的数据类型：【" + trdOrderDomain.getType() + "】");
			result.setResultObj(null);
			break;
		}

		return result;
	}

	@Transactional
	public BackResult<ErpTradeDomain> recharge(TrdOrderDomain trdOrderDomain, CreUserAccount creUserAccount) {

		BackResult<ErpTradeDomain> result = new BackResult<ErpTradeDomain>();

		try {

			// 检查订单编号防止重复提交
			List<TrdOrder> orderList = trdOrderMapper.findByClOrderNo(trdOrderDomain.getClOrderNo());
			
			if (!CommonUtils.isNotEmpty(orderList)) {
				result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
				result.setResultMsg("该订单已经处理，不能重复处理");
				result.setResultObj(null);
				return result;
			}

			TrdOrder order = new TrdOrder();

			// 保存充值记录
			BeanUtils.copyProperties(trdOrderDomain, order);
			
			if (trdOrderDomain.getProductsId() == 1) {

				if (new BigDecimal(950).equals(trdOrderDomain.getMoney())) {
					order.setNumber(500000);
					order.setMoney(new BigDecimal(950));
				} else {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("产品1充值金额必须为950");
					result.setResultObj(null);
					return result;
				}

			}

			if (trdOrderDomain.getProductsId() == 2) {

				if (new BigDecimal(9000).equals(trdOrderDomain.getMoney())) {
					order.setNumber(5000000); // 根据条数计算 具体金额
					order.setMoney(new BigDecimal(9000));
				} else {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("产品2充值金额必须为9000");
					result.setResultObj(null);
					return result;
				}

			}

			if (trdOrderDomain.getProductsId() == 3) {

				if (new BigDecimal(16000).equals(trdOrderDomain.getMoney())) {
					order.setNumber(10000000); // 根据条数计算 具体金额
					order.setMoney(new BigDecimal(16000));
				} else {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("产品3充值金额必须为16000");
					result.setResultObj(null);
					return result;
				}

			}

			if (trdOrderDomain.getProductsId() == 4) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setAccount(creUserAccount.getAccount() + order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 8) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setRqAccount(creUserAccount.getRqAccount() + order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 12) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setTcAccount(creUserAccount.getTcAccount() + order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 16) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setFcAccount(creUserAccount.getFcAccount() + order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 20) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setMsAccount(creUserAccount.getMsAccount()==null?0:creUserAccount.getMsAccount() + order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 24) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setCtAccount(creUserAccount.getCtAccount()==null?0:creUserAccount.getCtAccount() + order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 28) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setFiAccount(creUserAccount.getFiAccount()==null?0:creUserAccount.getFiAccount() + order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 32) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setFfAccount(creUserAccount.getFfAccount()==null?0:creUserAccount.getFfAccount() + order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 36) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setClAccount(creUserAccount.getClAccount()==null?0:creUserAccount.getClAccount() + order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 40) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setIdocrAccount(creUserAccount.getIdocrAccount()==null?0:creUserAccount.getIdocrAccount() + order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 44) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setBlocrAccount(creUserAccount.getBlocrAccount()==null?0:creUserAccount.getBlocrAccount() + order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 48) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setBocrAccount(creUserAccount.getBocrAccount()==null?0:creUserAccount.getBocrAccount() + order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 52) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setDocrAccount(creUserAccount.getDocrAccount()==null?0:creUserAccount.getDocrAccount() + order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 56) {
				order.setMoney(trdOrderDomain.getMoney());
				order.setNumber(order.getNumber());
				creUserAccount.setFcAccount(creUserAccount.getFcAccount() + order.getNumber());
			}

			order.setCreUserId(trdOrderDomain.getCreUserId());
			order.setCreateTime(new Date());
			order.setUpdateTime(new Date());
			order.setPayType("3");
			order.setStatus(Constant.TRD_ORDER_STATUS_SUCCEED);
			order.setVersion(0);
			
			trdOrderMapper.saveTrdOrder(order);
			BackResult<String> agentIdResult = agentService.getAgentIdByCreUserId(trdOrderDomain.getCreUserId().toString());
			if(!ResultCode.RESULT_SUCCEED.equals(agentIdResult.getResultCode())){
				result.setResultCode(ResultCode.RESULT_FAILED);
				result.setResultMsg("数据落地异常");
				result.setResultObj(null);
				return result;
			}
			Map<String,Object> param = new HashMap<>();
			param.put("trdOrderId", order.getId());
			param.put("agentId", agentIdResult.getResultObj());
			//保存数据到代理商支付映射表
			int counts = agentMapper.saveAgentTrdOrder(param);
			if(counts != 1){
				result.setResultCode(ResultCode.RESULT_FAILED);
				result.setResultMsg("数据落地异常");
				result.setResultObj(null);
				return result;
			}
			
			logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行充值，充值金额：【" + trdOrderDomain.getMoney()
					+ "】，充值条数；【" + order.getNumber() + "】");
			
			// 账户充值
			this.updateCreUserAccount(creUserAccount);
			//充值redis的余额
			this.updateCreUserAccountRedis(order.getCreUserId(),trdOrderDomain.getProductsId(),order.getNumber());
			
			ErpTradeDomain domain = new ErpTradeDomain();
			domain.setMoney(trdOrderDomain.getMoney());
			domain.setCount(order.getNumber());
			result.setResultObj(domain);
		} catch (Exception e) {
			logger.error("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行充值，充值金额：【" + trdOrderDomain.getMoney()
					+ "】，充值条数；【" + trdOrderDomain.getNumber() + "】，发生系统异常：" + e.getMessage());
			e.printStackTrace();
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("数据落地异常");
			result.setResultObj(null);
		}

		return result;
	}

	@Transactional
	public BackResult<ErpTradeDomain> refunds(TrdOrderDomain trdOrderDomain, CreUserAccount creUserAccount) {

		BackResult<ErpTradeDomain> result = new BackResult<ErpTradeDomain>();

		try {

			// 检查订单编号防止重复提交
			List<TrdOrder> orderList = trdOrderMapper.findByClOrderNo(trdOrderDomain.getClOrderNo());

			if (!CommonUtils.isNotEmpty(orderList)) {
				result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
				result.setResultMsg("该订单已经处理，不能重复处理");
				result.setResultObj(null);
				return result;
			}

			TrdOrder order = new TrdOrder();

			// 保存流水记录
			BeanUtils.copyProperties(trdOrderDomain, order);

			order.setMoney(trdOrderDomain.getMoney());
			order.setNumber(trdOrderDomain.getNumber());
			order.setCreUserId(creUserAccount.getCreUserId());
			order.setCreateTime(new Date());
			order.setUpdateTime(new Date());
			order.setStatus(Constant.TRD_ORDER_STATUS_SUCCEED);
			order.setPayType("3");
			order.setVersion(0);

			// 账户退款
			if (trdOrderDomain.getProductsId() == 4) {
				if (creUserAccount.getAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setAccount(creUserAccount.getAccount() - order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 8) {
				if (creUserAccount.getRqAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getRqAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setRqAccount(creUserAccount.getRqAccount() - order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 12) {
				if (creUserAccount.getTcAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getTcAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setTcAccount(creUserAccount.getTcAccount() - order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 16) {
				if (creUserAccount.getFcAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getFcAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setFcAccount(creUserAccount.getFcAccount() - order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 20) {
				if (creUserAccount.getMsAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getMsAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setMsAccount(creUserAccount.getMsAccount() - order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 24) {
				if (creUserAccount.getCtAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getCtAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setCtAccount(creUserAccount.getCtAccount() - order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 28) {
				if (creUserAccount.getFiAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getFiAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setFiAccount(creUserAccount.getFiAccount() - order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 32) {
				if (creUserAccount.getFfAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getFfAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setFfAccount(creUserAccount.getFfAccount() - order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 36) {
				if (creUserAccount.getClAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getClAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setClAccount(creUserAccount.getClAccount() - order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 40) {
				if (creUserAccount.getIdocrAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getIdocrAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setIdocrAccount(creUserAccount.getIdocrAccount() - order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 44) {
				if (creUserAccount.getBlocrAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getBlocrAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setBlocrAccount(creUserAccount.getBlocrAccount() - order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 48) {
				if (creUserAccount.getBocrAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getBocrAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setBocrAccount(creUserAccount.getBocrAccount() - order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 52) {
				if (creUserAccount.getDocrAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getDocrAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setDocrAccount(creUserAccount.getDocrAccount() - order.getNumber());
			}
			
			if (trdOrderDomain.getProductsId() == 56) {
				if (creUserAccount.getFcAccount() < order.getNumber()) {
					result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
					result.setResultMsg("账户余额不足，本次退款失败！");

					logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
							+ "】，退款条数；【" + order.getNumber() + "】；当前账户剩余条数【" + creUserAccount.getFcAccount() + "】不支持本次退款");
					result.setResultObj(null);

					return result;
				}
				creUserAccount.setFcAccount(creUserAccount.getFcAccount() - order.getNumber());
			}
			
			BackResult<String> agentIdResult = agentService.getAgentIdByCreUserId(trdOrderDomain.getCreUserId().toString());
			if(!ResultCode.RESULT_SUCCEED.equals(agentIdResult.getResultCode())){
				result.setResultCode(ResultCode.RESULT_FAILED);
				result.setResultMsg("数据落地异常");
				result.setResultObj(null);
				return result;
			}

			this.updateCreUserAccount(creUserAccount);
			trdOrderMapper.saveTrdOrder(order);
			
			Map<String,Object> param = new HashMap<>();
			param.put("trdOrderId", order.getId());
			param.put("agentId", agentIdResult.getResultObj());
			//保存数据到代理商支付映射表
			int counts = agentMapper.saveAgentTrdOrder(param);
			if(counts != 1){
				result.setResultCode(ResultCode.RESULT_FAILED);
				result.setResultMsg("数据落地异常");
				result.setResultObj(null);
				return result;
			}
			//充值redis的余额
			this.updateCreUserAccountRedis(order.getCreUserId(),trdOrderDomain.getProductsId(),-order.getNumber());
			
			logger.info("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
					+ "】，退款条数；【" + order.getNumber() + "】");

			ErpTradeDomain domain = new ErpTradeDomain();
			domain.setMoney(trdOrderDomain.getMoney());
			domain.setCount(order.getNumber());
			result.setResultObj(domain);
		} catch (Exception e) {
			logger.error("用户ID：【" + trdOrderDomain.getCreUserId() + "】执行退款，退款金额：【" + trdOrderDomain.getMoney()
					+ "】，退款条数；【" + trdOrderDomain.getNumber() + "】，发生系统异常：" + e.getMessage());
			e.printStackTrace();
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("数据落地异常");
			result.setResultObj(null);
		}

		return result;
	}

	@Override
	public BackResult<List<TrdOrderDomain>> findTrdOrderByCreUserId(Integer creUserId) {

		BackResult<List<TrdOrderDomain>> result = new BackResult<List<TrdOrderDomain>>();

		List<TrdOrderDomain> list = new ArrayList<TrdOrderDomain>();

		try {

			List<TrdOrder> orderList = trdOrderMapper.findByCreUserId(creUserId);

			if (CommonUtils.isNotEmpty(orderList)) {
				result.setResultMsg("改用户没有订单信息");
			}

			for (TrdOrder trdOrder : orderList) {
				TrdOrderDomain domain = new TrdOrderDomain();
				BeanUtils.copyProperties(trdOrder, domain);
				list.add(domain);
			}

			result.setResultObj(list);

		} catch (Exception e) {
			logger.error("用户ID：【" + creUserId + "】查询订单信息发生系统异常：" + e.getMessage());
			e.printStackTrace();
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("数据落地异常");
		}

		return result;
	}

	@Transactional
	public BackResult<Boolean> consumeAccount(String creUserId, String count) {

		BackResult<Boolean> result = new BackResult<Boolean>();

		CreUserAccount account = this.findCreUserAccountByUserId(Integer.valueOf(creUserId));

		try {
			if (null == account) {
				logger.error("用户ID：【" + creUserId + "】，不存在账户记录，数据落地异常");
				result.setResultCode(ResultCode.RESULT_DATA_EXCEPTIONS);
				result.setResultObj(Boolean.FALSE);
				result.setResultMsg("数据库不存在账户记录");
				return result;
			}

			if (Integer.valueOf(count) > account.getAccount()) {
				logger.error("用户ID：【" + creUserId + "】，当前用户余额条数不够本次消费");
				result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
				result.setResultObj(Boolean.FALSE);
				result.setResultMsg("当前用户余额条数不够本次消费");
				return result;
			}

			account.setAccount(account.getAccount() - Integer.valueOf(count));
			account.setUpdateTime(new Date());
			creUserAccountMapper.updateCreUserAccount(account);
			result.setResultObj(Boolean.TRUE);
		} catch (Exception e) {
			logger.error("用户ID：【" + creUserId + "】查询修改账户信息系统异常：" + e.getMessage());
			e.printStackTrace();
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("数据落地异常");
		}

		return result;
	}

	@Transactional
	public BackResult<Boolean> consumeApiAccount(String creUserId, String count) {

		BackResult<Boolean> result = new BackResult<Boolean>();

		CreUserAccount account = this.findCreUserAccountByUserId(Integer.valueOf(creUserId));

		try {
			if (null == account) {
				logger.error("用户ID：【" + creUserId + "】，不存在账户记录，数据落地异常");
				result.setResultCode(ResultCode.RESULT_API_NOTACCOUNT);
				result.setResultObj(Boolean.FALSE);
				result.setResultMsg("API商户信息不存在，或者已经删除请联系数据中心客户人员！");
				return result;
			}

			if (Integer.valueOf(count) > account.getApiAccount()) {
				logger.error("用户ID：【" + creUserId + "】，当前用户余额条数不够本次消费");
				result.setResultObj(Boolean.FALSE);
				result.setResultCode(ResultCode.RESULT_API_NOTCOUNT);
				result.setResultMsg("API商户信息剩余可消费条数为：" + account.getAccount() + "本次执行消费：" + count + "无法执行消费，请充值！");
				return result;
			}

			account.setApiAccount(account.getApiAccount() - Integer.valueOf(count));
			account.setUpdateTime(new Date());
			creUserAccountMapper.updateCreUserAccount(account);
			result.setResultObj(Boolean.TRUE);
			logger.info("用户id" + creUserId + "进行空号API本次成功消费：" + count + "条，剩余："
					+ (account.getApiAccount() - Integer.valueOf(count)) + "条");
		} catch (Exception e) {
			logger.error("用户ID：【" + creUserId + "】查询修改账户信息系统异常：" + e.getMessage());
			e.printStackTrace();
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("数据落地异常");
		}

		return result;
	}

	@Override
	public BackResult<Boolean> consumeRqApiAccount(String creUserId, String count) {

		BackResult<Boolean> result = new BackResult<Boolean>();

		CreUserAccount account = this.findCreUserAccountByUserId(Integer.valueOf(creUserId));

		try {
			if (null == account) {
				logger.error("用户ID：【" + creUserId + "】，不存在账户记录，数据落地异常");
				result.setResultCode(ResultCode.RESULT_API_NOTACCOUNT);
				result.setResultObj(Boolean.FALSE);
				result.setResultMsg("账户二次清洗API商户信息不存在，或者已经删除请联系数据中心客户人员！");
				return result;
			}

			if (Integer.valueOf(count) > account.getRqAccount()) {
				logger.error("用户ID：【" + creUserId + "】，当前用户余额条数不够本次消费");
				result.setResultObj(Boolean.FALSE);
				result.setResultCode(ResultCode.RESULT_API_NOTCOUNT);
				result.setResultMsg("账户二次清洗商户信息剩余可消费条数为：" + account.getAccount() + "本次执行消费：" + count + "无法执行消费，请充值！");
				return result;
			}

			account.setRqAccount(account.getRqAccount() - Integer.valueOf(count));
			account.setUpdateTime(new Date());
			creUserAccountMapper.updateCreUserAccount(account);
			result.setResultObj(Boolean.TRUE);
			logger.info("用户id" + creUserId + "进行账户2次清洗本次成功消费：" + count + "条，剩余："
					+ (account.getRqAccount() - Integer.valueOf(count)) + "条");
		} catch (Exception e) {
			logger.error("用户ID：【" + creUserId + "】查询修改账户信息系统异常：" + e.getMessage());
			e.printStackTrace();
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("数据落地异常");
		}

		return result;
	}

	@Override
	public BackResult<PageDomain<Map<String,Object>>> pageFindTrdOrderByCreUserId(Integer creUserId, Integer pageSize,
			Integer pageNum) {
		BackResult<PageDomain<Map<String,Object>>> result = new BackResult<PageDomain<Map<String,Object>>>();
		PageDomain<Map<String,Object>> listDomain = new PageDomain<Map<String,Object>>();
		listDomain.setTotalPages(0);
		listDomain.setCurrentPage(1);
		listDomain.setNumPerPage(10);
		listDomain.setTotalNumber(0);
		Map<String, Object> map = new HashMap<>();
		int totalNumber = 0; // 总条数
		int totalPages=0;   //总页数
		try {
			map.put("creUserId", creUserId);   //用户号码
			map.put("numPerPage", pageSize); // 显示行数
			map.put("currentPage",(pageNum-1)*pageSize); // 当前页码
			totalNumber = trdOrderMapper.quertCountTrdOrder(creUserId); // 总条数
			if (totalNumber == 0) {
				result.setResultMsg("该用户没有订单信息");
				result.setResultObj(listDomain);
				return result;
			} else {
			   List<Map<String,Object>> orderList = trdOrderMapper.pageFindTrdOrderByCreUserId(map);
			   totalPages=(totalNumber/pageSize);
			   if ((totalNumber%pageSize)!=0) {
				   totalPages++;
			    }

				listDomain.setTotalPages(totalPages);
				listDomain.setCurrentPage(pageNum);
				listDomain.setNumPerPage(pageSize);
				listDomain.setTotalNumber(totalNumber);
				listDomain.setTlist(orderList);
				result.setResultObj(listDomain);
			}
		} catch (Exception e) {
			logger.error("用户ID：【" + creUserId + "】查询订单信息发生系统异常：" + e.getMessage());
			e.printStackTrace();
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("数据落地异常");
		}

		return result;
	}
	
	@Override
	public BackResult<String> updateResultPwd(String mobile, String resultPwd) {
		//返回结果
		BackResult<String> result = new BackResult<String>();
		ApiAccountInfo apiAccountInfo = new ApiAccountInfo();
		try {
			//获取用户信息
			List<CreUser> user = creUserMapper.findCreUserByUserPhone(mobile);
			if (CommonUtils.isNotEmpty(user)) {
				result.setResultMsg("系统未查询到该用户");
				result.setResultCode(ResultCode.RESULT_FAILED);
				return result;
			}
			//查看用户帐号信息是否已经生成
			List<ApiAccountInfo> apiAccountInfoList = apiAccountInfoMapper.findByCreUserId(user.get(0).getId());
			if(apiAccountInfoList != null && apiAccountInfoList.size()>0){
				BeanUtils.copyProperties(apiAccountInfoList.get(0), apiAccountInfo);
				apiAccountInfo.setResultPwd(resultPwd);
				apiAccountInfo.setUpdateTime(new Date());
				//修改用户信息
				int counts = apiAccountInfoMapper.update(apiAccountInfo);
				if(counts != 1){
					result.setResultMsg("修改密码失败，数据库异常");
					result.setResultCode(ResultCode.RESULT_FAILED);
					return result;
				}
			}else{
				apiAccountInfo.setCreUserId(user.get(0).getId());
				apiAccountInfo.setName("S" + UUIDTool.getInstance().getUUID8());
				apiAccountInfo.setPassword("pwd" + UUIDTool.getInstance().getUUID8());
				apiAccountInfo.setResultPwd(resultPwd);
				apiAccountInfo.setCreateTime(new Date());
				apiAccountInfo.setUpdateTime(new Date());
				//保存用户信息
				int counts = apiAccountInfoMapper.save(apiAccountInfo);
				if(counts != 1){
					result.setResultMsg("修改密码失败，数据库异常");
					result.setResultCode(ResultCode.RESULT_FAILED);
					return result;
				}
			}			
		} catch (Exception e) {
			logger.error("用户手机号：【" + mobile + "】执行修改检测结果压缩包密码发生系统异常：" + e.getMessage());
			e.printStackTrace();
			result.setResultCode(ResultCode.RESULT_FAILED);
			result.setResultMsg("系统异常");
		}
		
		result.setResultObj(resultPwd);
		return result;
	}
	
	@Override
	public BackResult<String> getResultPwd(String creUserId) {
		BackResult<String> result = new BackResult<String>();
		String resultPwd = apiAccountInfoMapper.getResultPwd(creUserId);
		result.setResultObj(resultPwd);
		return result;
	}
	
	private void updateCreUserAccountRedis(Integer userId,Integer products_id,Integer counts){
    	String keys = "";
    	String key = "";
    	if(products_id == 8) {
			// 账号二次清洗充值回调
    		keys = RedisKeys.getInstance().getRQAPIcountKeys();
    		key = RedisKeys.getInstance().getRQAPIcountKey(userId.toString());
		} else if (products_id == 4) {
            // 空号API充值回调
            keys = RedisKeys.getInstance().getKHAPIcountKeys();
            key = RedisKeys.getInstance().getKHAPIcountKey(userId.toString());
        } else if (products_id == 12) {
			// 证件号码一致性校验回调
        	keys = RedisKeys.getInstance().getIdCardAuthcountKeys();
            key = RedisKeys.getInstance().getIdCardAuthAPIcountKey(userId.toString());
		} else if (products_id == 16) {
            // 银行卡四要素认证回调
			keys = RedisKeys.getInstance().getBankAuthcountKeys();
            key = RedisKeys.getInstance().getBankAuthAPIcountKey(userId.toString());
        } else if (products_id == 20) {
            // 运营商在线号码状态查询回调
        	keys = RedisKeys.getInstance().getMsAPIcountKeys();
            key = RedisKeys.getInstance().getMsAPIcountKey(userId.toString());
        }else if (products_id == 24) {
            // 运营商三要素回调
        	keys = RedisKeys.getInstance().getMobileThreeAuthcountKeys();
            key = RedisKeys.getInstance().getMobileThreeAuthAPIcountKey(userId.toString());
        }else if (products_id == 28) {
            // 人证比对回调
        	keys = RedisKeys.getInstance().getFiApicountKeys();
            key = RedisKeys.getInstance().getFiApicountKey(userId.toString());
        }else if (products_id == 32) {
            // 一比一人脸比对回调
        	keys = RedisKeys.getInstance().getFfApicountKeys();
            key = RedisKeys.getInstance().getFfApicountKey(userId.toString());
        }else if (products_id == 36) {
            // 活体检测回调
        	keys = RedisKeys.getInstance().getClApicountKeys();
            key = RedisKeys.getInstance().getClApicountKey(userId.toString());
        }else if (products_id == 40) {
            // 身份证ocr回调
        	keys = RedisKeys.getInstance().getIdocrApicountKeys();
            key = RedisKeys.getInstance().getIdocrApicountKey(userId.toString());
        }else if (products_id == 44) {
            // 营业执照ocr回调
        	keys = RedisKeys.getInstance().getBlocrApicountKeys();
            key = RedisKeys.getInstance().getBlocrApicountKey(userId.toString());
        }else if (products_id == 48) {
            //银行卡ocr回调
        	keys = RedisKeys.getInstance().getBocrApicountKeys();
            key = RedisKeys.getInstance().getBocrApicountKey(userId.toString());
        }else if (products_id == 52) {
            // 驾驶证ocr回调
        	keys = RedisKeys.getInstance().getDocrApicountKeys();
            key = RedisKeys.getInstance().getDocrApicountKey(userId.toString());
        }else if (products_id == 56) {
            // 银行卡鉴权精细版回调
        	keys = RedisKeys.getInstance().getBankAuthcountKeys();
            key = RedisKeys.getInstance().getBankAuthAPIcountKey(userId.toString());
        }
    	
    	if(StringUtils.isNotBlank(keys)){
    		String redisKeys = redisClient.get(keys)==null?null:redisClient.get(keys);
    		if(StringUtils.isNotBlank(redisKeys)){
    			if(redisKeys.contains(userId.toString()+",")){
        			String redisKey = redisClient.get(key)==null?"0":redisClient.get(key);
        			if(StringUtils.isNotBlank(redisKey)){
        				Integer tempCounts = Integer.parseInt(redisKey)+counts;
        				redisClient.set(key, tempCounts<0?"0":tempCounts.toString());
        			}
        		}
    		}    		
    	}
    }
}
