package cn.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dao.BaseLogMapper;
import cn.entity.BaseLog;
import cn.service.BaseLogService;

@Service
public class BaseLogServiceImpl implements BaseLogService {

	private final static Logger logger = LoggerFactory.getLogger(BaseLogServiceImpl.class);
	
	@Autowired
	private BaseLogMapper baseLogMapper;

	@Override
	public int saveBaseLog(int userId, String erpId, String operateTable, String operation, String content,
			String ip) {
		BaseLog baseLog = new BaseLog();
		baseLog.setUserId(userId);
		baseLog.setErpId(erpId);
		baseLog.setOperateTable(operateTable);
		baseLog.setOperation(operation);
		baseLog.setContent(content);
		baseLog.setIp(ip);
		return baseLogMapper.saveBaseLog(baseLog);
	}

}
