package cn.redis;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;  

@Component 
public class RedisClient {
	
	@Autowired  
    private JedisPool jedisPool;  
      
    public void set(String key, String value) {  
        Jedis jedis = null;  
        try {
        	jedis = jedisPool.getResource();  
            jedis.set(key, value);
//            jedis.expire(key, 30 * 60);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			//返还到连接池  
            jedis.close(); 
		}
    }  
    
    public Set<String> keys(String pattern) {
    	Set<String> result = new HashSet<String>();
        try (Jedis jedis = jedisPool.getResource()) {
            result = jedis.keys(pattern);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //返还到连接池
        return result;
    }
    
    /**
     * 设置key 同时设置到期时间
     * @param key
     * @param value
     * @param seconds
     */
    public void set(String key, String value,int seconds) {  
        Jedis jedis = null;  
        try {
        	jedis = jedisPool.getResource();  
            jedis.set(key, value);
            jedis.expire(key, seconds);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			//返还到连接池  
            jedis.close(); 
		}
    }  
      
    public String get(String key)  {  
  
        Jedis jedis = null;  
        String value = "";
        try {
        	jedis = jedisPool.getResource();  
        	value = jedis.get(key);  
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			 //返还到连接池  
            jedis.close();  
		}
        
        return value;
    }  
    
    public long incrBy(String key,Long value) {    	
    	long result = 0;
        try (Jedis jedis = jedisPool.getResource()) {
        	result = jedis.incrBy(key, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //返还到连接池
        return result;
    }
    
    public long decrBy(String key,Long value) {
    	long result = 0;
        try (Jedis jedis = jedisPool.getResource()) {
            result = jedis.decrBy(key, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //返还到连接池
        return result;
    }
    
    public void remove(String key) {
    	Jedis jedis = null;  
        try {
        	jedis = jedisPool.getResource();  
        	jedis.del(key);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			 //返还到连接池  
            jedis.close();  
		}
        
    }
}
