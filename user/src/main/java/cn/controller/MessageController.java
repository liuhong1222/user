package cn.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.exception.ProcessException;
import cn.service.MessageService;

@RestController
@RequestMapping("/message")
public class MessageController{

	@Autowired
	private MessageService messageService;
	
	private final static Logger logger = LoggerFactory.getLogger(MessageController.class);
	
	/**
	 * 消息列表
	 * @return
	 */
	@RequestMapping("/messageList")
	public List<Map<String,Object>> messageList(HttpServletRequest request, HttpServletResponse response,String customer_id,String isRead,int offset,int pageSize) {		
		return messageService.messageList(customer_id,isRead,offset,pageSize);
	}
	
	/**
	 * 消息列表数量
	 * @return
	 */
	@RequestMapping("/getMessageListCount")
	public long getMessageListCount(HttpServletRequest request, HttpServletResponse response,String customer_id,String isRead) {		
		return messageService.getMessageListCount(customer_id,isRead);
	}
	
	/**
	 * 获取用户未读消息个数
	 * @return
	 */
	@RequestMapping("/getNoReadMessageCount")
	public long getNoReadMessageCount(HttpServletRequest request, HttpServletResponse response,String customer_id) {		
		return messageService.getNoReadMessageCount(customer_id);
	}
	
	/**
	 * 修改用户消息状态为已读
	 * @return
	 */
	@RequestMapping("/updateMessageStatus")
	public int updateMessageStatus(HttpServletRequest request, HttpServletResponse response,String customer_id) {		
		return messageService.updateMessageStatus(customer_id);
	}
	
	/**
	 * 修改用户消息状态为已读
	 * @return
	 */
	@RequestMapping("/deleteMessage")
	public int deleteMessage(HttpServletRequest request, HttpServletResponse response,String customer_id,String messageStr) {		
		return messageService.deleteMessage(customer_id,messageStr);
	}
	
	/**
	 * 查看消息
	 * @return
	 */
	@Transactional
	@RequestMapping("/readMessage")
	public Map<String,Object> readMessage(HttpServletRequest request, HttpServletResponse response,String customer_id,String message_id) {	
		//根据消息id获取message信息
		Map<String,Object> message = messageService.getMessageById(message_id);
		if(message == null){
			throw new ProcessException("操作失败,数据库异常");
		}
		int counts = 0;
		if("0".equals(message.get("isread").toString())){
			Map<String,Object> params = new HashMap<>();
			params.put("customer_id", customer_id);
			params.put("message_id", message_id);
			//修改消息状态为已读
			counts = messageService.updateMessageStatusById(params);
			if(counts != 1){
				throw new ProcessException("操作失败,数据库异常");
			}
		}			
		return message;
	}
}
