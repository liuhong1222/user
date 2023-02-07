package cn.service;

import java.util.List;
import java.util.Map;

/**
 * 用户消息
 *
 */
public interface MessageService{
		
	List<Map<String,Object>> messageList(String customer_id,String isRead,int offset,int pageSize);
	
	long getNoReadMessageCount(String customer_id);
	
	long getMessageListCount(String customer_id,String isRead);
	
	int saveMessage(String id,String customer_id,String title,String type,String message);
	
	int updateMessageStatus(String customer_id);
	
	int deleteMessage(String customer_id,String messageStr);
	
	int updateMessageStatusById(Map<String,Object> params);
	
	Map<String,Object> getMessageById(String message_id);
}
