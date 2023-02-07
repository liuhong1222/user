package cn.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.service.NewsService;

@RestController
@RequestMapping("/news")
public class NewsController{

	@Autowired
	private NewsService newsService;
	
	private final static Logger logger = LoggerFactory.getLogger(NewsController.class);
	
	/**
	 * 消息列表
	 * @return
	 */
	@RequestMapping("/newsList")
	public List<Map<String,Object>> newsList(HttpServletRequest request, HttpServletResponse response,String domain,int offset,int pageSize) {		
		return newsService.newsList(domain,offset,pageSize);
	}
	
	/**
	 * 消息列表数量
	 * @return
	 */
	@RequestMapping("/getNewsListCount")
	public long getNewsListCount(HttpServletRequest request, HttpServletResponse response,String domain) {		
		return newsService.getNewsListCount(domain);
	}
	
	/**
	 * 获取最新的两个新闻
	 * @return
	 */
	@RequestMapping("/getTop2News")
	public List<Map<String,Object>> getTop2News(HttpServletRequest request, HttpServletResponse response) {		
		return newsService.getTop2News();
	}
	
	/**
	 * 查看消息
	 * @return
	 */
	@Transactional
	@RequestMapping("/readNews")
	public Map<String,Object> readNews(HttpServletRequest request, HttpServletResponse response,String news_id) {	
		//根据消息id获取message信息
		Map<String,Object> message = newsService.getNewsById(news_id);
		return message;
	}
	
	/**
	 * 获取关于我们的新闻内容
	 * @return
	 */
	@Transactional
	@RequestMapping("/getAboutMeContent")
	public Map<String,Object> getAboutMeContent(HttpServletRequest request, HttpServletResponse response,String domain) {	
		//获取关于我们的新闻内容
		Map<String,Object> message = newsService.getAboutMeContent(domain);
		return message;
	}
}
