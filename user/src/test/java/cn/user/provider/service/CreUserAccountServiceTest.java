package cn.user.provider.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.UserProviderServiceApp;
import cn.entity.CreUserAccount;
import cn.service.CreUserAccountService;
import main.java.cn.common.BackResult;
import main.java.cn.domain.TrdOrderDomain;
import main.java.cn.domain.page.PageDomain;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=UserProviderServiceApp.class)// 指定spring-boot的启动类   
public class CreUserAccountServiceTest {
	
	@Autowired  
    private CreUserAccountService creUserAccountService;  
	
	//@Test  
    public void saveCreUser()  {  
    	CreUserAccount creUserAccount = new CreUserAccount();
    	creUserAccount.setCreUserId(1575);
		creUserAccount.setCreateTime(new Date());
		creUserAccount.setUpdateTime(new Date());
		int row = creUserAccountService.saveCreUserAccount(creUserAccount);
		System.out.println(row);
    } 
	
//	@Test  
    public void findAllUsers()  {  
		CreUserAccount creUserAccount = creUserAccountService.findCreUserAccountByUserId(1575);
		creUserAccount.setAccount(10000);
		creUserAccount.setUpdateTime(new Date());
		int row = creUserAccountService.updateCreUserAccount(creUserAccount);
		System.out.println(row);
    } 
    
    @Test
    public void pageFindCreUser(){
    	Integer creUserId=1600;
    	Integer numPage=5;
    	Integer curPage=3;
    	BackResult<PageDomain<Map<String,Object>>>  page=creUserAccountService
    			.pageFindTrdOrderByCreUserId(creUserId, numPage, curPage);
    	System.out.println(page.getResultObj());
    	List<Map<String,Object>> list=page.getResultObj().getTlist();
    	for(Map<String,Object> tod:list){
    		System.out.println(tod+"=========");
    	}
    }

}
