package cn.user.provider.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.UserProviderServiceApp;
import cn.entity.ApiAccountInfo;
import cn.service.ApiAccountInfoService;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=UserProviderServiceApp.class)// 指定spring-boot的启动类   
public class ApiAccountServiceTest {
	
	@Autowired  
    private ApiAccountInfoService apiAccountInfoService;
	
	@Test  
    public void saveCreUser()  {  
    	ApiAccountInfo info = new ApiAccountInfo();
    	info.setCreUserId(1604);
    	info.setBdIp("1111111");
    	info.setId(1);
    	info.setVersion(0);
//		int row = apiAccountInfoService.update(info);
//		System.out.println(row);
    } 
	

}
