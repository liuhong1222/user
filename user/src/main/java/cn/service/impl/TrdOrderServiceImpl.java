package cn.service.impl;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;

import cn.dao.AgentMapper;
import cn.dao.CreUserAccountMapper;
import cn.dao.CreUserMapper;
import cn.dao.InviteDetailMapper;
import cn.dao.TrdOrderMapper;
import cn.dao.UserInviteInfoMapper;
import cn.entity.AgentAccount;
import cn.entity.AgentAlipayInfo;
import cn.entity.AgentOrder;
import cn.entity.Contract;
import cn.entity.CreUser;
import cn.entity.CreUserAccount;
import cn.entity.TrdOrder;
import cn.exception.ProcessException;
import cn.redis.RedisClient;
import cn.service.AgentService;
import cn.service.ContractService;
import cn.service.MessageService;
import cn.service.TrdOrderService;
import cn.utils.CommonUtils;
import cn.utils.Constant;
import cn.utils.DateUtils;
import cn.utils.UUIDTool;
import main.java.cn.common.BackResult;
import main.java.cn.common.RedisKeys;
import main.java.cn.common.ResultCode;
import main.java.cn.domain.AgentCreUserDomain;
import main.java.cn.domain.AgentWebSiteDomain;
import main.java.cn.domain.InviteDetailDomain;
import main.java.cn.domain.PackageDomain;
import main.java.cn.domain.TrdOrderDomain;
import main.java.cn.domain.UserInviteInfoDomain;
import main.java.cn.enums.ProductPayTypeEnum;
import main.java.cn.hhtp.util.HttpUtil;
import main.java.cn.hhtp.util.MD5Util;
import main.java.cn.sms.util.ChuangLanSmsUtil;
import main.java.cn.sms.util.MarketingSmsUtil;
import main.java.cn.until.GenerateUtil;

@Service
public class TrdOrderServiceImpl implements TrdOrderService {

	private final static Logger logger = LoggerFactory.getLogger(TrdOrderServiceImpl.class);

	@Autowired
	private TrdOrderMapper trdOrderMapper;

	@Autowired
	private CreUserAccountMapper creUserAccountMapper;

	@Autowired
	private CreUserMapper creUserMapper;
	
	@Autowired
	private AgentMapper agentMapper;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private InviteDetailMapper inviteDetailMapper;
	
	@Autowired
	private UserInviteInfoMapper userInviteInfoMapper;
	
	@Value("${withCreditProviderService}")
	private String withCreditProviderService;

	@Value("${getPdfFileByHtmlUrl}")
	private String getPdfFileByHtmlUrl;
	
	@Override
	public TrdOrder findByOrderNo(String orderNo) {
		List<TrdOrder> list = trdOrderMapper.findByOrderNo(orderNo);
		return CommonUtils.isNotEmpty(list) ? null : list.get(0);
	}

	@Override
	public TrdOrder findByClOrderNo(String clOrderNo) {
		List<TrdOrder> list = trdOrderMapper.findByClOrderNo(clOrderNo);
		return CommonUtils.isNotEmpty(list) ? null : list.get(0);
	}

	@Override
	public int saveTrdOrder(TrdOrder trdOrder) {
		return trdOrderMapper.saveTrdOrder(trdOrder);
	}

	@Override
	public int updateTrdOrder(TrdOrder trdOrder) {
		return trdOrderMapper.updateTrdOrder(trdOrder);
	}

	@Transactional
	public synchronized void payCallBack(String orderNo, String status, String traOrder) {

		// ?????????????????????
		TrdOrder order = this.findByOrderNo(orderNo);
		//?????????????????????
		int freeNumber = 0;
		
		if (null == order) {
			logger.error("????????????" + orderNo + "???????????????????????????");
		}

		if ("TRADE_SUCCESS".equals(status)) {

			// ?????????????????????????????? ????????????
			order.setStatus(Constant.TRD_ORDER_STATUS_SUCCEED);
			order.setTradeNo(traOrder); // ??????????????????
			order.setPayTime(new Date()); // ??????????????????
			order.setUpdateTime(new Date()); 
			this.updateTrdOrder(order);
			
			// ????????????????????????
			List<CreUserAccount> accountList = creUserAccountMapper.findCreUserAccountByUserId(order.getCreUserId());
			CreUserAccount account = accountList.get(0);
			
			if(orderNo.substring(0,4).equals("CLRQ")) {
				// ??????????????????????????????
                account.setRqAccount(account.getRqAccount() + order.getNumber());
			} else if (orderNo.substring(0,4).equals("CLSH")) {
				// ????????????????????????
				account.setAccount(account.getAccount() + order.getNumber() + freeNumber);
			} else if (orderNo.substring(0,4).equals("CLKH")) {
                // ??????API????????????
                account.setApiAccount(account.getApiAccount() + order.getNumber());
            } else if (orderNo.substring(0,4).equals("CLTC")) {
				// ?????????????????????????????????
				account.setTcAccount(account.getTcAccount() + order.getNumber());
			} else if (orderNo.substring(0,4).equals("CLFC")) {
                // ??????????????????????????????
                account.setFcAccount(account.getFcAccount() + order.getNumber());
            } else if (orderNo.substring(0,4).equals("CLMS")) {
                // ???????????????????????????????????????
                account.setMsAccount(account.getMsAccount() + order.getNumber());
            }else if (orderNo.substring(0,4).equals("CLCT")) {
                // ????????????????????????
                account.setCtAccount(account.getCtAccount() + order.getNumber());
            }else if (orderNo.substring(0,4).equals("CLFI")) {
                // ??????????????????
                account.setFiAccount(account.getFiAccount() + order.getNumber());
            }else if (orderNo.substring(0,4).equals("CLFF")) {
                // ???????????????????????????
                account.setFfAccount(account.getFfAccount() + order.getNumber());
            }else if (orderNo.substring(0,4).equals("CLCL")) {
                // ??????????????????
                account.setClAccount(account.getClAccount() + order.getNumber());
            }else if (orderNo.substring(0,4).equals("CLIO")) {
                // ?????????ocr??????
                account.setIdocrAccount(account.getIdocrAccount() + order.getNumber());
            }else if (orderNo.substring(0,4).equals("CLLO")) {
                // ????????????ocr??????
                account.setBlocrAccount(account.getBlocrAccount() + order.getNumber());
            }else if (orderNo.substring(0,4).equals("CLBO")) {
                //?????????ocr??????
                account.setBocrAccount(account.getBocrAccount() + order.getNumber());
            }else if (orderNo.substring(0,4).equals("CLDO")) {
                // ?????????ocr??????
                account.setDocrAccount(account.getDocrAccount() + order.getNumber());
            }else if (orderNo.substring(0,4).equals("FCJX")) {
                //???????????????????????????
                account.setFcAccount(account.getFcAccount() + order.getNumber());
            }
			
			creUserAccountMapper.updateCreUserAccount(account);
			this.updateCreUserAccountRedis(order.getCreUserId(),orderNo,order.getNumber()+freeNumber);
			AgentWebSiteDomain agentInfo = agentMapper.getAgentInfoByCreUserId(order.getCreUserId().toString());
			String message = "?????????????????????????????????????????????" + order.getMoney() + "???,??????" + (order.getNumber()+freeNumber) + "???????????????????????????????????????";
			messageService.saveMessage(UUIDTool.getInstance().getUUID(), order.getCreUserId().toString(), "????????????", "1", message);
			AgentAccount agentAccount =  agentMapper.getAgentAccountByAgentId(agentInfo.getAgentId());
			if(agentAccount != null){
				int tempNum = Integer.parseInt(redisClient.get(order.getOrderNo())==null?"0":redisClient.get(order.getOrderNo()));				
				AgentOrder agentOrder = new AgentOrder();
				agentOrder.setAgentId(agentInfo.getAgentId());
				agentOrder.setProductId(order.getProductsId());
				agentOrder.setOrderNo(orderNo);
				agentOrder.setTradeNo(traOrder);
				agentOrder.setType(3);
				agentOrder.setPrice(order.getMoney().multiply(new BigDecimal(100)).divide(new BigDecimal(order.getNumber()),2).setScale(4, RoundingMode.HALF_UP));
				agentOrder.setNumber(order.getNumber()-tempNum);
				agentOrder.setMoney(order.getMoney());
				agentOrder.setPayTime(new Date());
				agentOrder.setPayType(1);
				agentOrder.setStatus(1);
				agentOrder.setRemark("");
				agentOrder.setRoleType(2);
				agentOrder.setVersion(0);
				agentMapper.saveAgentOrder(agentOrder);
				logger.info("?????????" + order.getCreUserId()+ "???????????? ??????????????????" + agentInfo.getAgentId() + "????????????????????????????????????");
			}
			
		} else {
			order.setStatus(Constant.TRD_ORDER_STATUS_FAILED);
			this.updateTrdOrder(order);
		}

	}

	@Transactional
	public synchronized BackResult<String> recharge(Integer creUserId,Integer productsId,Integer number,BigDecimal money,String payType,String type,String userType) {
		
		BackResult<String> result = new BackResult<String>();
		try {
			Integer tempNumber = 1;
			BigDecimal tempMoneyNumber = new BigDecimal(1);
			if(productsId % 4 ==0){
				tempNumber = number;
				tempMoneyNumber = new BigDecimal(number);
			}
			String timestamp = String.valueOf(System.currentTimeMillis());
			// ????????????
			TrdOrder order = new TrdOrder();
			order.setCreUserId(creUserId);
			order.setProductsId(productsId);
			//??????????????????????????????
			BackResult<String> agentIdResult = agentService.getAgentIdByCreUserId(creUserId.toString());
			if(!ResultCode.RESULT_SUCCEED.equals(agentIdResult.getResultCode())){
				throw new ProcessException("??????????????????????????????????????????");
			}			
			//??????????????????????????????
			Map<String,String> paramPack = new HashMap<>();
			paramPack.put("agentId", agentIdResult.getResultObj());
			paramPack.put("productId", productsId.toString());
			BackResult<Map<String,Object>> agentPackage = agentService.getAgentPackage(paramPack);
			if(!ResultCode.RESULT_SUCCEED.equals(agentIdResult.getResultCode())){
				throw new ProcessException("?????????????????????????????????????????????");
			}
//			//??????????????????????????????
//			ProductPayTypeEnum ppte = ProductPayTypeEnum.getProductPayTypeEnum(productsId);
			order.setNumber(tempNumber * Integer.parseInt(agentPackage.getResultObj().get("number").toString()));
			order.setMoney(tempMoneyNumber.multiply(new BigDecimal(agentPackage.getResultObj().get("money").toString())).setScale(2, BigDecimal.ROUND_HALF_UP));
			String orderCode = agentPackage.getResultObj().get("orderCode").toString();
			String subjectName = agentPackage.getResultObj().get("packageName").toString();			
			
			//???????????????????????????
//			BackResult<String> agentIdBalance = agentService.getAgentBalanceByAgentId(creUserId.toString());
//			if(!ResultCode.RESULT_SUCCEED.equals(agentIdBalance.getResultCode())){
//				throw new ProcessException("???????????????????????????????????????????????????????????????");
//			}
//			int balance = Integer.parseInt(agentIdBalance.getResultObj());
//			if(balance <= 0 || balance < order.getNumber()){
//				throw new ProcessException("????????????????????????????????????????????????????????????");
//			}
			
			order.setPayType(payType);
			order.setType(type);
			order.setOrderNo(orderCode + timestamp);
			order.setStatus(Constant.TRD_ORDER_STATUS_PROCESSING);
			order.setDeleteStatus("0");
			order.setVersion(0);
			order.setCreateTime(new Date());
			order.setUpdateTime(new Date());
			this.saveTrdOrder(order);
						
			Map<String,Object> param = new HashMap<>();
			param.put("trdOrderId", order.getId());
			param.put("agentId", agentIdResult.getResultObj());
			//???????????????????????????????????????
			int counts = agentMapper.saveAgentTrdOrder(param);
			if(counts != 1){
				throw new ProcessException("???????????????????????????????????????");
			}
			//??????????????????????????????
			String agentId = null;
			String display = "??????";
			if (StringUtils.isNotBlank(redisClient.get(Constant.REDIS_KEY+creUserId))) {
					agentId = "0";
					display = "display";
			}else {
				if(Integer.valueOf(agentIdResult.getResultObj()) > 3) {
					agentId = agentIdResult.getResultObj();
					display = "display";
				}
			}
			AgentAlipayInfo alipayInfos = agentMapper.getAgentAlipayInfos(StringUtils.isBlank(agentId)?agentIdResult.getResultObj():agentId);
			if(alipayInfos == null){
				throw new ProcessException("?????????????????????????????????????????????");
			}
			
			// ???????????????????????? ???????????????
			AlipayClient alipayClient = new DefaultAlipayClient(alipayInfos.getAlipayPayurl(), alipayInfos.getAlipayAppid(), alipayInfos.getAlipayPrivatekey(),"json","utf-8", alipayInfos.getAlipayPublickey(), "RSA2");
			AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest(); // ??????API?????????request???
			String subject = subjectName;
			String storeId = "????????????";
			request.setBizContent("{" + "    \"out_trade_no\":\""+order.getOrderNo()+"\"," + "    \"total_amount\":\""+order.getMoney()+"\","
					+ "    \"subject\":\""+subject+"\"," + "    \"store_id\":\""+storeId+"\","
							+ "    \"timeout_express\":\"90m\"}");// ??????????????????

			request.setNotifyUrl(alipayInfos.getAlipayCallbackurl());
			
			logger.info("?????????????????????{out_trade_no:"+order.getOrderNo()+"total_amount:"+order.getMoney()+"subject:"+subject+"store_id:"+storeId+"}");
			
			AlipayTradePrecreateResponse response = alipayClient.execute(request);

			if (response.isSuccess()) {
				
				JSONObject responseBody = JSONObject.parseObject(response.getBody());
				
				JSONObject json = JSONObject.parseObject(responseBody.get("alipay_trade_precreate_response").toString());
				
				if (json.get("code").equals("10000")) {
					//????????????
					JSONObject resultObj = new JSONObject();
					resultObj.put("payUrl", json.get("qr_code").toString());
					resultObj.put("orderNo", order.getOrderNo());
					result.setResultObj(resultObj.toString());
					result.setResultCode(ResultCode.RESULT_SUCCEED);
					result.setResultMsg(display);
				}
				
			} else {
				logger.error("????????????????????????" + response.getBody() + "???");
				result.setResultCode(ResultCode.RESULT_BUSINESS_EXCEPTIONS);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("??????????????????????????????" + e.getMessage() + "???");
			result.setResultCode(ResultCode.RESULT_FAILED);
		}
		
		return result;
	}
	
	@Override
	public BackResult<TrdOrderDomain> findOrderInfoByOrderNo(String orderNo) {
		
		BackResult<TrdOrderDomain> result = new BackResult<TrdOrderDomain>();
		
		TrdOrder order = this.findByOrderNo(orderNo);
		
		if (null == order) {
			result.setResultCode(ResultCode.RESULT_DATA_EXCEPTIONS);
			result.setResultMsg("??????????????????");
		} else {
			
			TrdOrderDomain trdOrderDomain = new TrdOrderDomain();
			BeanUtils.copyProperties(order, trdOrderDomain);
			result.setResultObj(trdOrderDomain);
		}
		
		return result;
	}

	@Override
	public BackResult<List<PackageDomain>> getPayPackage(Integer creUserId,String productId) {
		BackResult<List<PackageDomain>> result = new BackResult<List<PackageDomain>>();
		Map<String,Object> param = new HashMap<>();
		param.put("creUserId", creUserId.toString());
		param.put("productId", productId);
		List<PackageDomain> packageDomain = trdOrderMapper.getPayPackage(param);
		if(packageDomain == null || packageDomain.size()<= 0){
			result.setResultCode(ResultCode.RESULT_DATA_EXCEPTIONS);
			result.setResultMsg("???????????????????????????????????????");
			return result;
		}
		result.setResultObj(packageDomain);
		return result;
	}
	
	@Override
	public BackResult<List<Map<String, Object>>> getSummyOrderList(String startDate, String endDate) {
		BackResult<List<Map<String, Object>>> result = new BackResult<List<Map<String, Object>>>();
		Map<String,Object> param = new HashMap<>();
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		List<Map<String, Object>> orderList = trdOrderMapper.getSummyOrderList(param);
		if(orderList == null || orderList.size()<= 0){
			result.setResultCode(ResultCode.RESULT_DATA_EXCEPTIONS);
			result.setResultMsg("???????????????????????????????????????");
			return result;
		}
		result.setResultObj(orderList);
		return result;
	}
	
	
    private void updateCreUserAccountRedis(Integer userId,String orderNo,Integer counts){
    	String keys = "";
    	String key = "";
    	if(orderNo.substring(0,4).equals("CLRQ")) {
			// ??????????????????????????????
    		keys = RedisKeys.getInstance().getRQAPIcountKeys();
    		key = RedisKeys.getInstance().getRQAPIcountKey(userId.toString());
		} else if (orderNo.substring(0,4).equals("CLKH")) {
            // ??????API????????????
            keys = RedisKeys.getInstance().getKHAPIcountKeys();
            key = RedisKeys.getInstance().getKHAPIcountKey(userId.toString());
        } else if (orderNo.substring(0,4).equals("CLTC")) {
			// ?????????????????????????????????
        	keys = RedisKeys.getInstance().getIdCardAuthcountKeys();
            key = RedisKeys.getInstance().getIdCardAuthAPIcountKey(userId.toString());
		} else if (orderNo.substring(0,4).equals("CLFC")) {
            // ??????????????????????????????
			keys = RedisKeys.getInstance().getBankAuthcountKeys();
            key = RedisKeys.getInstance().getBankAuthAPIcountKey(userId.toString());
        } else if (orderNo.substring(0,4).equals("CLMS")) {
            // ???????????????????????????????????????
        	keys = RedisKeys.getInstance().getMsAPIcountKeys();
            key = RedisKeys.getInstance().getMsAPIcountKey(userId.toString());
        }else if (orderNo.substring(0,4).equals("CLCT")) {
            // ????????????????????????
        	keys = RedisKeys.getInstance().getMobileThreeAuthcountKeys();
            key = RedisKeys.getInstance().getMobileThreeAuthAPIcountKey(userId.toString());
        }else if (orderNo.substring(0,4).equals("CLFI")) {
            // ??????????????????
        	keys = RedisKeys.getInstance().getFiApicountKeys();
            key = RedisKeys.getInstance().getFiApicountKey(userId.toString());
        }else if (orderNo.substring(0,4).equals("CLFF")) {
            // ???????????????????????????
        	keys = RedisKeys.getInstance().getFfApicountKeys();
            key = RedisKeys.getInstance().getFfApicountKey(userId.toString());
        }else if (orderNo.substring(0,4).equals("CLCL")) {
            // ??????????????????
        	keys = RedisKeys.getInstance().getClApicountKeys();
            key = RedisKeys.getInstance().getClApicountKey(userId.toString());
        }else if (orderNo.substring(0,4).equals("CLIO")) {
            // ?????????ocr??????
        	keys = RedisKeys.getInstance().getIdocrApicountKeys();
            key = RedisKeys.getInstance().getIdocrApicountKey(userId.toString());
        }else if (orderNo.substring(0,4).equals("CLLO")) {
            // ????????????ocr??????
        	keys = RedisKeys.getInstance().getBlocrApicountKeys();
            key = RedisKeys.getInstance().getBlocrApicountKey(userId.toString());
        }else if (orderNo.substring(0,4).equals("CLBO")) {
            //?????????ocr??????
        	keys = RedisKeys.getInstance().getBocrApicountKeys();
            key = RedisKeys.getInstance().getBocrApicountKey(userId.toString());
        }else if (orderNo.substring(0,4).equals("CLDO")) {
            // ?????????ocr??????
        	keys = RedisKeys.getInstance().getDocrApicountKeys();
            key = RedisKeys.getInstance().getDocrApicountKey(userId.toString());
        }else if (orderNo.substring(0,4).equals("FCJX")) {
            // ??????????????????????????????
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
        				redisClient.set(key, tempCounts.toString());
        			}
        		}
    		}    		
    	}
    }
}
