package cn.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.service.TrdOrderService;
import main.java.cn.common.BackResult;
import main.java.cn.domain.PackageDomain;
import main.java.cn.domain.TrdOrderDomain;

@RestController
@RequestMapping("/trdorder")
public class TrdorderController {

	@Autowired
	private TrdOrderService trdOrderService;
	
	@RequestMapping("/alipayrecharge")
	public BackResult<String> alipayrecharge(Integer creUserId,Integer productsId,Integer number,BigDecimal money,String payType,String type,String userType){
		return trdOrderService.recharge(creUserId,productsId,number,money,payType,type,userType);
	}
	
	@RequestMapping("/findOrderInfoByOrderNo")
	public BackResult<TrdOrderDomain> findOrderInfoByOrderNo(String orderNo){
		return trdOrderService.findOrderInfoByOrderNo(orderNo);
	}
	
	@RequestMapping("/getPayPackage")
	public BackResult<List<PackageDomain>> getPayPackage(Integer creUserId,String productId){
		return trdOrderService.getPayPackage(creUserId,productId);
	}
	
	@RequestMapping("/getSummyOrderList")
	public BackResult<List<Map<String,Object>>> getSummyOrderList(String startDate,String endDate){
		return trdOrderService.getSummyOrderList(startDate,endDate);
	}
}
