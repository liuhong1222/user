package cn.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper {
	
	List<Map<String,Object>> newsList(Map<String,Object> param);
	
	long getNewsListCount(String domain);
	
	Map<String,Object> getNewsById(String news_id);
	
	List<Map<String,Object>> getTop2News();
	
	Map<String,Object> getAboutMeContent(String domain);
}
