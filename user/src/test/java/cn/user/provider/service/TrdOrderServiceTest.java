
package cn.user.provider.service;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.UserProviderServiceApp;
import cn.entity.TrdOrder;
import cn.service.TrdOrderService;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=UserProviderServiceApp.class)// 指定spring-boot的启动类   
public class TrdOrderServiceTest {
	
	@Autowired  
    private TrdOrderService trdOrderService;  
	
//	@Test  
    public void saveCreUser()  {  
		TrdOrder trdOrder = new TrdOrder();
		trdOrder.setCreUserId(1575);
//		trdOrder.setOrderNo("12345698774125896321478521478521");
		trdOrder.setClOrderNo("22345698774125896321478521478521");
		trdOrder.setNumber(5000);
		trdOrder.setMoney(new BigDecimal(100));
		trdOrder.setPayTime(new Date());
		trdOrder.setPayType("1");
		trdOrder.setType("2");
		trdOrder.setStatus("0");
		trdOrder.setCreateTime(new Date());
		trdOrder.setUpdateTime(new Date());
		int row = trdOrderService.saveTrdOrder(trdOrder);
		System.out.println(row);
    } 
	
	@Test  
    public void findByClOrderNo()  {  
		TrdOrder trdOrder = trdOrderService.findByClOrderNo("22345698774125896321478521478521");
		trdOrder.setStatus("1");
		trdOrder.setPayTime(new Date());
		trdOrder.setUpdateTime(new Date());
		int row = trdOrderService.updateTrdOrder(trdOrder);
		System.out.println(row);
    } 

}
