package cn.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.dao.MessageMapper;
import cn.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	private final static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

	@Autowired
	private MessageMapper messageMapper;

	@Override
	public List<Map<String, Object>> messageList(String customer_id,String isRead,int offset,int pageSize) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("customer_id", customer_id);
		param.put("isRead", isRead);
		param.put("offset", offset);
		param.put("pageSize", pageSize);
		return messageMapper.messageList(param);
	}

	@Override
	public long getNoReadMessageCount(String customer_id) {
		return messageMapper.getNoReadMessageCount(customer_id);
	}

	@Override
	public int updateMessageStatus(String customer_id) {
		return messageMapper.updateMessageStatus(customer_id);
	}

	@Override
	public Map<String, Object> getMessageById(String message_id) {
		return messageMapper.getMessageById(message_id);
	}

	@Override
	public int updateMessageStatusById(Map<String,Object> params) {
		return messageMapper.updateMessageStatusById(params);
	}

	@Override
	public long getMessageListCount(String customer_id,String isRead) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("customer_id", customer_id);
		param.put("isRead", isRead);
		return messageMapper.getMessageListCount(param);
	}

	@Override
	public int deleteMessage(String customer_id, String messageStr) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("customer_id", customer_id);
		param.put("messageStr", messageStr);
		return messageMapper.deleteMessage(param);
	}

	@Override
	public int saveMessage(String id,String customer_id,String title,String type,String message) {
		Map<String, Object> params = new HashMap<>();
		params.put("id",id);
		params.put("customer_id",customer_id);
		params.put("title",title);
		params.put("type",type);
		params.put("message",message);	
		return messageMapper.saveMessage(params);
	}

}
