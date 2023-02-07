package cn.user.provider.service;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.UserProviderServiceApp;
import cn.entity.CreUser;
import cn.service.CreUserService;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=UserProviderServiceApp.class)// 指定spring-boot的启动类   
public class CreUserServiceTest {
	
	@Autowired  
    private CreUserService creUserService;  
	
	@Test  
    public void saveCreUser()  {  
		CreUser creUser = new CreUser();
		creUser.setClAccountId(22233);
		creUser.setCompanyId(333);
		creUser.setUserPhone("15115367247");
		creUser.setUserName("宋宇22");
		creUser.setCreateTime(new Date());
		creUser.setUpdateTime(new Date());
		int row = creUserService.saveCreUser(creUser);
		System.out.println(row);
    } 
	
//	@Test  
    public void findAllUsers()  {  
		CreUser creUser = creUserService.findCreUserByUserPhone("15115367247");
		creUser.setClAccountId(22233);
		creUser.setCompanyId(333);
		creUser.setUserName("宋宇S");
		creUser.setUpdateTime(new Date());
		creUser.setId(1575);
		int row = creUserService.updateCreUser(creUser);
		System.out.println(row);
    } 

}
