package cn.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.dao.NewsMapper;
import cn.service.NewsService;

@Service
public class NewsServiceImpl implements NewsService {

	private final static Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

	@Autowired
	private NewsMapper newsMapper;

	@Override
	public List<Map<String, Object>> newsList(String domain,int offset, int pageSize) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("offset", offset);
		param.put("pageSize", pageSize);
		param.put("domain", domain);
		return newsMapper.newsList(param);
	}

	@Override
	public long getNewsListCount(String domain) {
		return newsMapper.getNewsListCount(domain);
	}

	@Override
	public List<Map<String, Object>> getTop2News() {
		return newsMapper.getTop2News();
	}

	@Override
	public Map<String, Object> getNewsById(String news_id) {
		return newsMapper.getNewsById(news_id);
	}

	@Override
	public Map<String, Object> getAboutMeContent(String domain) {
		return newsMapper.getAboutMeContent(domain);
	}
}
