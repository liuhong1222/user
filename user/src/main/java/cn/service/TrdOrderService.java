package cn.service;

import cn.entity.TrdOrder;
import main.java.cn.service.TrdOrderBusService;

/**
 * CreUserService
 *
 */
public interface TrdOrderService extends TrdOrderBusService{
	
	TrdOrder findByOrderNo(String orderNo);
	
	TrdOrder findByClOrderNo(String clOrderNo);

	int saveTrdOrder(TrdOrder trdOrder);
	
	int updateTrdOrder(TrdOrder trdOrder);
	
	void payCallBack(String orderNo,String status,String traOrder);
}
