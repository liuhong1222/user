package cn.service;

import java.util.List;
import java.util.Map;

/**
 * 新闻
 *
 */
public interface NewsService{
		
	List<Map<String,Object>> newsList(String domain,int offset,int pageSize);
		
	long getNewsListCount(String domain);
	
	List<Map<String,Object>> getTop2News();
	
	Map<String,Object> getNewsById(String news_id);
	
	Map<String,Object> getAboutMeContent(String domain);
}
