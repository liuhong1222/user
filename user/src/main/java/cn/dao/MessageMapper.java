package cn.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {
	
	List<Map<String,Object>> messageList(Map<String,Object> param);
	
	long getNoReadMessageCount(String customer_id);
	
	long getMessageListCount(Map<String,Object> param);
	
	int saveMessage(Map<String,Object> params);
	
	int updateMessageStatus(String customer_id);
	
	int deleteMessage(Map<String,Object> param);
	
	Map<String,Object> getMessageById(String message_id);
	
	int updateMessageStatusById(Map<String,Object> params);
}
