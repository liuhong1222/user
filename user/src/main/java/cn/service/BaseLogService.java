package cn.service;

/**
 * erp客户信息
 *
 */
public interface BaseLogService{
		
	int saveBaseLog(int userId,String erpId,String operateTable,String operation,String content,String ip);
	
}
