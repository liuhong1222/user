package cn.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.dao.AgentMapper;
import cn.dao.CreUserWarningMapper;
import cn.entity.CreUserAccount;
import cn.service.CreUserAccountService;
import cn.service.MessageService;
import cn.utils.DateUtils;
import cn.utils.UUIDTool;
import main.java.cn.domain.AgentWebSiteDomain;
import main.java.cn.domain.CreUserWarningDomain;
import main.java.cn.sms.util.ChuangLanSmsUtil;

/**
 * 账户余额提醒
 *
 */
@Component
@Configuration
@EnableScheduling
public class ApiCountRemindingTask {
	
	private final static Logger logger = LoggerFactory.getLogger(ApiCountRemindingTask.class);
	
	@Autowired
	private CreUserWarningMapper creUserWarningMapper;
	
	@Autowired
	private AgentMapper agentMapper;
	
	@Autowired
	private CreUserAccountService creUserAccountService;
	
	@Autowired
	private MessageService messageService;
	
	@Value("${cljobchange}")
	String cljobchange;
	
	/**
	 * 账户余额提醒
	 * 
	 * @throws IOException
	 */
	@Scheduled(cron = "0 0 0/1 * * ?")
	public void creUserAccountReminding() throws IOException {
		if (cljobchange.equals("produced")) {
			logger.info("<<<<<<<<<<开始余额提醒任务>>>>>>>>>>");
			//获取所有用户设置的预警值
			List<CreUserWarningDomain> warningList = creUserWarningMapper.getCreUserWarningList();
			if(warningList != null && warningList.size() > 0){
				for(CreUserWarningDomain cuwd : warningList){
					int creUserId = cuwd.getCreUserId();
					String productName = cuwd.getProductName();
					int warningCount = cuwd.getWarningCount();
					String informMobiles = cuwd.getInformMobiles();
					int informNums = cuwd.getInformNums();
					//获取用户所有产品的实时余额
					CreUserAccount cua = creUserAccountService.findCreUserAccountByUserId(creUserId);
					//获取用户所属代理商短信签名
					AgentWebSiteDomain signed = agentMapper.getAgentIdByDomain(cuwd.getCreUserId().toString());
					String sign = "创蓝数据应用中心";
					if(signed != null && StringUtils.isNotBlank( signed.getSmsSign())){
						sign = signed.getSmsSign();
					}
					if(cua != null){
						int productCount = getProductAccount(cua,productName);
						if(warningCount >= productCount){
							if(informNums >= 0 && informNums < 3){
								//产品余额低于或等于预警值且提醒次数小于3次的发送短信提醒
								String[] mobileList = informMobiles.split(",");
								for(String mobile : mobileList){	
									//发送短信提醒
									ChuangLanSmsUtil.getInstance().sendSmsByAccountRemain(sign,mobile, productCount, warningCount,getProductAccount(productName));
									//用户该产品提醒值加1
									Map<String,Object> param = new HashMap<>();
									param.put("informNums", informNums + 1);
									param.put("userId", creUserId);
									param.put("productName", productName);
									creUserWarningMapper.updateCreUserWarning(param);
									//发送消息提醒
									String message = "尊敬的客户您好：截止到" + DateUtils.getNowTime1() + "您的" + getProductAccount(productName) + "余额为：" + productCount + "条,不足" + warningCount + "条,为了不影响业务，请及时充值！";
									messageService.saveMessage(UUIDTool.getInstance().getUUID(), creUserId+"", "余额提醒", "1", message);
									logger.info("--------用户【" + creUserId + "】余额提醒成功, " + getProductAccount(productName) + "产品余额为：" + productCount + "条,不足" + warningCount + "条");
								}								
							}
						}else{
							if(informNums > 0){
								Map<String,Object> param = new HashMap<>();
								param.put("informNums", 0);
								param.put("userId", creUserId);
								param.put("productName", productName);
								//产品余额高于预警值且已发过短信提醒则初始化提醒此时为0
								creUserWarningMapper.updateCreUserWarning(param);
								logger.info("--------用户【" + creUserId + "】余额提醒次数初始化成功--------");
							}
						}
					}
				}
			}
			
			logger.info("<<<<<<<<<<结束余额提醒任务>>>>>>>>>>");
		}
	}
	
	private int getProductAccount(CreUserAccount cua,String productName){
		int count = 0;
		if("account".equals(productName)){
			count = cua.getAccount();
		}else if("apiAccount".equals(productName)){
			count = cua.getApiAccount();
		}else if("rqAccount".equals(productName)){
			count = cua.getRqAccount();
		}else if("tcAccount".equals(productName)){
			count = cua.getTcAccount();
		}else if("fcAccount".equals(productName)){
			count = cua.getFcAccount();
		}else if("msAccount".equals(productName)){
			count = cua.getMsAccount();
		}else if("ctAccount".equals(productName)){
			count = cua.getCtAccount();
		}else if("fiAccount".equals(productName)){
			count = cua.getFiAccount();
		}else if("ffAccount".equals(productName)){
			count = cua.getFfAccount();
		}else if("clAccount".equals(productName)){
			count = cua.getClAccount();
		}else if("idocrAccount".equals(productName)){
			count = cua.getIdocrAccount();
		}else if("blocrAccount".equals(productName)){
			count = cua.getBlocrAccount();
		}else if("bocrAccount".equals(productName)){
			count = cua.getBocrAccount();
		}else if("docrAccount".equals(productName)){
			count = cua.getDocrAccount();
		}
		
		return count;		
	}
	
	private String getProductAccount(String productName){
		String name = "";
		if("account".equals(productName)){
			name = "空号在线检测产品";
		}else if("apiAccount".equals(productName)){
			name = "空号检测Api产品";
		}else if("rqAccount".equals(productName)){
			name = "二次清洗产品";
		}else if("tcAccount".equals(productName)){
			name = "身份认证二要素产品";
		}else if("fcAccount".equals(productName)){
			name = "银行卡鉴权产品";
		}else if("msAccount".equals(productName)){
			name = "号码实时在线查询产品";
		}else if("ctAccount".equals(productName)){
			name = "运营商三要素产品";
		}else if("fiAccount".equals(productName)){
			name = "人证比对产品";
		}else if("ffAccount".equals(productName)){
			name = "人脸比对产品";
		}else if("clAccount".equals(productName)){
			name = "活体检测产品";
		}else if("idocrAccount".equals(productName)){
			name = "身份证OCR产品";
		}else if("blocrAccount".equals(productName)){
			name = "营业执照OCR产品";
		}else if("bocrAccount".equals(productName)){
			name = "银行卡OCR产品";
		}else if("docrAccount".equals(productName)){
			name = "驾驶证OCR产品";
		}
		
		return name;		
	}
}
