package com.zhan.app.spider.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.zhan.app.common.News;
import com.zhan.app.common.NewsDetial;
import com.zhan.app.common.Video;
import com.zhan.app.spider.controller.NewsController;
import com.zhan.app.spider.dao.NewsDao;

@Service
public class NewsService {
	private static Logger log = Logger.getLogger(NewsController.class);
	@Resource
	private NewsDao newsDao;

	public String insert(News news) {
		if (news.url.startsWith("https://")) {
			news.url_type = 1;
		}
		return newsDao.save(news);
	}

	public long insert(NewsDetial news) {
		if (news.detial_url.startsWith("https://")) {
			news.url_type = 1;
		}
		newsDao.save(news);
		return 0;
	}

	public String insert(Video video) {
		return newsDao.save(video);
	}

	public synchronized boolean hasExist(News news) {
		return newsDao.hasExist(news);
	}

	public boolean hasExist(Video video) {
		long count = newsDao.getCount(video);
		return count > 0;
	}

	public List<?> list(String time, int limit) {
		return newsDao.list(time, limit);
	}

	public List<News> getNewsByUrl(String url) {
		return newsDao.getNewsByUrl(url);
	}

	public List<News> getNewsById(String id) {
		return newsDao.getNewsByID(id);
	}

	public void deleteByTitle(String title) {
		// TODO Auto-generated method stub
		newsDao.deleteByTitle(title);
	}

	public void deleteByID(String id) {
		newsDao.deleteByID(id);
	}

	public void deleteByUrl(String url) {
		newsDao.deleteByUrl(url);
	}
}
