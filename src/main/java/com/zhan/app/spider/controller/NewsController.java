package com.zhan.app.spider.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhan.app.common.News;
import com.zhan.app.spider.service.NewsService;
import com.zhan.app.spider.service.UserService;
import com.zhan.app.spider.spider.SpiderManager;
import com.zhan.app.spider.util.EncodeUtil;
import com.zhan.app.spider.util.ResultUtil;

@RestController
@RequestMapping("/spider")
public class NewsController {
	private static Logger log = Logger.getLogger(NewsController.class);

	@Resource
	private NewsService newsService;
	@Resource
	private UserService userService;
	@Resource
	protected RedisTemplate<String, String> redisTemplate;

	/**
	 * 获取注册用的短信验证码
	 * 
	 * @param request
	 * @param mobile
	 *            手机号码
	 * @return
	 */
	@RequestMapping("start")
	public ModelMap start(HttpServletRequest request, String spiderName) {
		new Thread() {
			@Override
			public void run() {
				try {
					SpiderManager.getInstance().start();
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}.start();
		return ResultUtil.getResultOKMap();
	}

	@RequestMapping("test_spider")
	public ModelMap test_spider(HttpServletRequest request, final String spiderName) {
		new Thread() {
			@Override
			public void run() {
				if (spiderName == null || "baidu".equals(spiderName)) {
					SpiderManager.getInstance().testBaidu();
				}
			}
		}.start();
		return ResultUtil.getResultOKMap();
	}

	@RequestMapping("delete_by_title")
	public ModelMap delete_by_title(HttpServletRequest request, String title) {
		newsService.deleteByTitle(title);
		return ResultUtil.getResultOKMap();
	}

	@RequestMapping("delete_by_url")
	public ModelMap delete_by_url(HttpServletRequest request, String url) {
		String encode = EncodeUtil.getEncoding(url);
		System.out.println(encode);
		List<News> toDels = newsService.getNewsById("583a606e3089a5333baae1c8");
		if (toDels != null && toDels.size() > 0) {
			System.out.println(EncodeUtil.getEncoding(toDels.get(0).url));
			System.out.println(url.equals(toDels.get(0).url));
		}
		List<News> toDel = newsService.getNewsByUrl(url);
		if (toDel != null && toDel.size() > 0) {
			newsService.deleteByUrl(url);
		}
		return ResultUtil.getResultOKMap();
	}

	@RequestMapping("delete_by_id")
	public ModelMap delete_by_id(HttpServletRequest request, String id) {
		newsService.deleteByID(id);
		return ResultUtil.getResultOKMap();
	}

	@RequestMapping("test")
	public ModelMap test(HttpServletRequest request, String url) {
		ModelMap result = ResultUtil.getResultOKMap();

		News news = new News();
		news.url = url;
		result.put("hasExist", newsService.hasExist(news));
		return result;
	}

	@RequestMapping("list")
	public List<News> list(HttpServletRequest request) {
		List<News> list = (List<News>) newsService.list("", 20);
		if (list != null) {
			System.out.println(EncodeUtil.getEncoding(list.get(0).url));
		}
		return list;
	}
}