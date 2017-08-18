package com.zhan.app.spider.spider.toutiao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Random;

import org.jsoup.nodes.Document;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.zhan.app.common.News;
import com.zhan.app.common.NewsDetial;
import com.zhan.app.spider.push.PushManager;
import com.zhan.app.spider.service.NewsService;
import com.zhan.app.spider.spider.toutiao.NewsDetialPraser;
import com.zhan.app.spider.spider.toutiao.NewsSimplePraser;
import com.zhan.app.spider.spider.toutiao.VideoSpider;
import com.zhan.app.spider.util.NotNullUtil;
import com.zhan.app.spider.util.RedisKeys;
import com.zhan.app.spider.util.SpiderPraser;
import com.zhan.app.spider.util.SpringContextUtil;

public class SpiderTouTiao {
	public static String[] urls = new String[] { "https://www.toutiao.com/ch/news_society/"
			,
			"https://www.toutiao.com/ch/news_entertainment/" 
			};
	private int videoCount = 0;

	private NewsSimplePraser simpleSpider;
	private NewsDetialPraser detialPraser;
	private VideoSpider videoSpider;

	public SpiderTouTiao() {
		initUtil();
	}

	private void initUtil() {
		if (simpleSpider == null) {
			simpleSpider = new NewsSimplePraser();
		}
		if (detialPraser == null) {
			detialPraser = new NewsDetialPraser();
		}
	}

	private String getRandomUrl() {
		int index = new Random().nextInt(urls.length);
		return urls[index];
	}

	private NewsService getNewsService() {
		return (NewsService) SpringContextUtil.getBean("newsService");
	}

	public void start() {
		String url = getRandomUrl();
		List<News> newsList = null;
		try {
			System.out.println(url);
			newsList = simpleSpider.prase(SpiderPraser.spiderContentByJsoup(SpiderPraser.spiderByPhantomjs(url)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (newsList == null || newsList.size() == 0) {
			return;
		}
		for (News news : newsList) {
			// 检测是否已经解析，有的话下一个
			if (getNewsService().hasExist(news)) {
				System.out.println("url=" + news.url);
				continue;
			}
			if (!NotNullUtil.canPrase(news)) {
				return;
			}
			try {
				detialSpider(news);
			} catch (Exception e) {
				e.printStackTrace();
				errorToMail(news, e);
				continue;
			}
		}
	}

	public void detialSpider(News news) throws MalformedURLException, IOException {

		NewsService newsService = getNewsService();

		RedisTemplate<String, Object> redisTemplate = SpringContextUtil.getBean("redisTemplate");

		Document doc = SpiderPraser.spiderByJsoup(news.url);
		if (!doc.text().contains(news.title)) {
			System.out.println("文不对题！");
			System.out.println(news.title);
			System.out.println(news.url);
			throw new RuntimeException("文不对题");
		}
		NewsDetial detialNews = detialPraser.prase(doc);
		if (detialNews == null) {
			return;
		}
		detialNews.title = news.title;
		detialNews.from = news.from;
		news.publish_time = detialNews.publish_time;
		String id = newsService.insert(news);
		detialNews.id = id;
		detialNews.detial_url = news.url;
		newsService.insert(detialNews);
		String newsStr = JSON.toJSONString(news);
		System.out.println(newsStr);
		redisTemplate.opsForValue().set(RedisKeys.KEY_NEW_NEWS, newsStr); // 新的变旧的

		PushManager.getInstance().extraPush(news);
	}

	private void errorToMail(News simpleNews, Exception e) {
		System.err.println("--------- error to mail report---------------" + simpleNews == null ? "" : simpleNews.url);
	}

	public void startVideoSpider() {
		if (videoCount == 20) {
			if (videoSpider == null) {
				videoSpider = new VideoSpider();
			}
			videoSpider.startSpider((NewsService) SpringContextUtil.getBean("newsService"));
			videoCount = 0;
		} else {
			videoCount++;
		}
	}

}
