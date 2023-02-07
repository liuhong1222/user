package cn.task;

import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import cn.entity.CreUserAccount;
import cn.redis.RedisClient;
import cn.service.CreUserAccountService;
import cn.utils.DateUtils;
import main.java.cn.until.RedisKeyUtil;

/**
 * API接口结算任务
 * 
 *
 */
@Component
@Configuration
@EnableScheduling
public class ApiCountSettlementTask {

	private final static Logger logger = LoggerFactory.getLogger(ApiCountSettlementTask.class);

	@Autowired
	private RedisClient redisClient;

	@Autowired
	private CreUserAccountService creUserAccountService;
	
	@Value("${cljobchange}")
	String cljobchange;


	/**
	 * 结算任务
	 * 
	 * @throws IOException
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	public void settlementKHApiCount() throws IOException {
		
		if (cljobchange.equals("produced")) {
			logger.info("<<<<<<<<<<开始结算定时任务>>>>>>>>>>");

			try {
				Set<String> keys = redisClient.keys(RedisKeyUtil.getApiSettlementPatternKey()+"*");
				if (CollectionUtils.isEmpty(keys)) {
					logger.info("无可结算账号");
					return ;
				}
				
				for (String key : keys) {
					// 获取redis中的需结算条数
					String settlementCounts = redisClient.get(key);
					Integer creUserId = Integer.valueOf(key.replace(RedisKeyUtil.getApiSettlementPatternKey(), ""));
					CreUserAccount account = creUserAccountService.findCreUserAccountByUserId(creUserId);						
					if (account == null) {
						logger.error("{}, 结算api余额失败，账号无余额记录，settlementCounts：{}",creUserId,settlementCounts);
						continue;
					}
					
					account.setApiAccount(Integer.parseInt(settlementCounts));							
					account.setUpdateTime(DateUtils.getCurrentDateTime());
					int counts = creUserAccountService.updateApiAccount(account);
					if (counts != 1) {
						logger.info("》》》》》用户[{}]本次API结算失败，数据库余额更改失败， 结算条数：{}",creUserId,settlementCounts);
						continue;
					}
					
					redisClient.decrBy(key, Long.valueOf(settlementCounts));
					logger.info("》》》》》用户[{}]本次API结算完成， 结算条数：{}",creUserId,settlementCounts);
				}
				
			} catch (Exception e) {
				logger.error("I结算定时任务>>>>>>>>>>出现系统异常：" , e);
			}

			logger.info("<<<<<<<<<<结束结算定时任务>>>>>>>>>>");
		}
		
	}
}
