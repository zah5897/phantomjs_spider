package com.zhan.app.spider.spider.baidu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhan.app.common.News;
import com.zhan.app.common.NewsDetial;
import com.zhan.app.common.node.Node;
import com.zhan.app.common.node.NodeType;
import com.zhan.app.spider.push.PushManager;
import com.zhan.app.spider.service.NewsService;
import com.zhan.app.spider.util.DateTimeUtil;
import com.zhan.app.spider.util.SpiderPraser;
import com.zhan.app.spider.util.SpringContextUtil;
import com.zhan.app.spider.util.TextUtils;

public class SpiderBaiDu {

	private static final String url = "https://m.baidu.com/?from=2001a";

	public void start() {
		List<News> newsSimple = getSimpleNews();

		if (newsSimple != null && newsSimple.size() > 0) {
			for (News news : newsSimple) {

				if (getNewsService().hasExist(news)) {
					System.out.println("---------exist to next--------------" + news.url);
					continue;
				}
				praseDetailNews(news);
			}
		}
	}

	private List<News> getSimpleNews() {
		List<News> news = null;
		try {
			String html = SpiderPraser.spiderByPhantomjs(url);
			Document doc = SpiderPraser.spiderContentByJsoup(html);
			news = new ArrayList<News>();

			Elements news_items = doc.select("div.news-item");
			for (Element item : news_items) {
				String detailUrl = item.child(0).attr("href");

				if (item.select("div.info-content").size() == 0) {
					System.out.println("图片新闻");
					continue;
				}

				String title = item.select("div.text-content").first().text();

				String iconUrl = item.select("div.pic-content img").first().attr("src");
				if (TextUtils.isEmpty(iconUrl)) {
					continue;
				}

				Element fromDiv = item.select("div.news-from").first();

				String from = fromDiv.select("span.src-net").first().text();
				String time = fromDiv.select("span.src-time").first().text();
				if (time.length() < 6) {
					if (time.contains(":")) {
						time = DateTimeUtil.getTodayStr() + " " + time;
					} else if (time.contains("-")) {
						Calendar c = Calendar.getInstance();
						c.setTime(new Date());
						int year = c.get(Calendar.YEAR);
						time = year + "-" + time;
					}
				}
				News simple = new News();
				simple.type = 1;
				simple.from = from;
				simple.icon = iconUrl;
				simple.news_abstract = title;
				simple.url = detailUrl;
				simple.title = title;
				try {
					simple.publish_time = getPublishTime(time);
					news.add(simple);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return news;
	}

	private void praseDetailNews(News news) {
		String html;
		try {
			html = SpiderPraser.spiderByPhantomjs(news.url);
			Document doc = SpiderPraser.spiderContentByJsoup(html);
			String detailTime = getDetailTime(doc);
			if (TextUtils.isEmpty(detailTime)) {
				detailTime = news.publish_time;
			}
			List<Node> nodes = getNodes(news.url, doc);

			if (nodes.size() < 2) {
				System.out.println("nodes is empty:url=" + news.url);
				return;
			}

			NewsDetial detialNews = new NewsDetial();
			detialNews.title = news.title;
			detialNews.from = news.from;
			news.publish_time = detailTime;
			String id = getNewsService().insert(news);
			detialNews.id = id;
			detialNews.detial_url = news.url;
			detialNews.type = 1;
			detialNews.nodes = nodes;
			getNewsService().insert(detialNews);
			PushManager.getInstance().extraPush(news);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private NewsService getNewsService() {
		return (NewsService) SpringContextUtil.getBean("newsService");
	}

	private String getDetailTime(Document doc) {
		Elements timeElements = doc.select("div.article-info");
		String time;
		if (timeElements.size() > 0) {
			String month_day = timeElements.first().select("span.info-date").first().text();
			String hour_minute = timeElements.first().select("span.info-time").first().text();
			time = DateTimeUtil.getYear() + "-" + month_day + " " + hour_minute;
			Date pubDate = DateTimeUtil.parseTouTiaoPublishTime(time);
			time = String.valueOf(pubDate.getTime() / 1000);
			return time;
		}
		return null;
	}

	private List<Node> getNodes(String url, Document doc) {
		List<Node> nodes = new ArrayList<Node>();

		// 第一种情况
		Elements contentDiv = doc.select("div.article-body");
		if (contentDiv.size() > 0) {
			Element body = contentDiv.first();
			Elements topImage = body.select("div.img-container");
			if (topImage.size() > 0) {
				String img = topImage.first().select("img").first().attr("src");
				nodes.add(new Node(NodeType.IMG.ordinal(), img));
			}
			Elements ps = body.select("p");
			for (Element p : ps) {
				if (p.select("img").size() > 0) {
					String pic = p.select("img").first().attr("src");
					nodes.add(new Node(NodeType.IMG.ordinal(), pic));
				} else {
					nodes.add(new Node(NodeType.TXT.ordinal(), p.text()));
				}

			}
			return nodes;
		}
		// 第二张情况
		// class="tc-card-imagetext-text"
		contentDiv = doc.select("div.tc-card-imagetext-text");

		int size = contentDiv.size();

		if (size == 3) {
			Element timeDiv = contentDiv.get(2);
			if (timeDiv.select("p").size() > 0) {
				Node node = getNode(timeDiv.select("p").first());
				if (!TextUtils.isEmpty(node.content)) {
					nodes.add(node);
				}
			} else {
				System.out.println("no content:url=" + url + "content=" + timeDiv);
			}
		} else {
			for (Element div : contentDiv) {
				if (div.select("p").size() > 0) {
					Node node = getNode(div.select("p").first());
					if (!TextUtils.isEmpty(node.content)) {
						nodes.add(node);
					}
				} else {
					System.out.println("no content:url=" + url + "content=" + div);
				}
			}
		}

		return nodes;
	}

	private Node getNode(Element p) {
		if (p.select("img").size() > 0) {
			String pic = p.select("img").first().attr("src");
			return new Node(NodeType.IMG.ordinal(), pic);
		} else {
			return new Node(NodeType.TXT.ordinal(), p.text());
		}
	}

	private String getPublishTime(String time) {
		Date publishTime = DateTimeUtil.parseBaiduPublishTime(time);
		if (publishTime == null) {
			publishTime = DateTimeUtil.parseBaiduPublishTime(DateTimeUtil.getTodayStr());
		}
		return String.valueOf(publishTime.getTime() / 1000);
	}

}
