package cn.utils;

import java.util.Random;
import java.util.UUID;

public class UUIDTool {
	
	
	private static UUIDTool uuidtool;
	
	public static UUIDTool getInstance() {  
        if (uuidtool == null) {    
            synchronized (UUIDTool.class) {    
               if (uuidtool == null) {    
            	   uuidtool = new UUIDTool();   
               }    
            }    
        }    
        return uuidtool;   
    }  
	
	/**  
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。  
     * @return  
     */  
    public String getUUID() {    
        return UUID.randomUUID().toString().replace("-", ""); 
    }  
    
    /**
     * 随机生成8位数
     * @return
     */
    public int getUUID8(){
    	int max=9999999;
        int min=1000000;
        Random random = new Random();
        return random.nextInt(max)%(max-min+1) + min;
    }
    
}
