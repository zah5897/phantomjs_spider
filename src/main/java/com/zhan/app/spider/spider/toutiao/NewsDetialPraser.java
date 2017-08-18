package com.zhan.app.spider.spider.toutiao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhan.app.common.NewsDetial;
import com.zhan.app.common.node.Node;
import com.zhan.app.spider.spider.BaseSpider;
import com.zhan.app.spider.util.DateTimeUtil;
import com.zhan.app.spider.util.SpiderPraser;
import com.zhan.app.spider.util.TextUtils;

public class NewsDetialPraser extends BaseSpider {

	private String baseUrl;

	public NewsDetial prase(Document doc) {

		NewsDetial newsDetial = new NewsDetial();

		String title = null;
		Date time = null;
		List<Node> nodes = null;

		Elements ignore = doc.select("div.video_detail");
		if (ignore.size() > 0) {
			throw new RuntimeException("detail page ignore,url=" + doc.baseUri());
		}
		Elements h1s = doc.select("h1.question-name");
		if (h1s.size() > 0) {
			throw new RuntimeException("detail page is question ,ignore,url=" + doc.baseUri());
		}

		Elements main = doc.select("div#article-main");
		Elements header = doc.select("header");
		if (main.size() > 0) {
			Elements titles = main.get(0).select("h1.article-title");
			if (titles.size() > 0) {
				title = getTrimContent(titles.get(0).text());
			}
			Elements timeSpan = main.get(0).select("span.time");
			if (timeSpan.size() > 0) {
				String timeStr = getTrimContent(timeSpan.get(0));
				time = DateTimeUtil.parseTouTiaoPublishTime(timeStr);
			}
			Elements content = main.get(0).select("div.article-content");
			if (content.size() > 0) {
				nodes = new ArrayList<Node>();
				praseNode(nodes, content.get(0));
			}

		} else if (header.size() > 0) {
			Elements titles = doc.select("h1.title");
			if (titles.size() > 0) {
				title = getTrimContent(titles.get(0));
			} else {
				titles = doc.select("h1");
				if (titles.size() > 0) {
					title = getTrimContent(titles.get(0));
				}
			}
			Elements subtitle = doc.select("div.subtitle>time");
			if (subtitle.size() > 0) {
				String timeStr = getTrimContent(subtitle.get(0));
				time = DateTimeUtil.parseTouTiaoPublishTime(timeStr);
				if (time == null) {
					if (timeStr.split("-").length == 2) {
						Calendar c = Calendar.getInstance();
						int year = c.get(Calendar.YEAR);
						timeStr = year + "-" + timeStr;
						time = DateTimeUtil.parseTouTiaoPublishTime(timeStr);
					}
				}
			}

			Elements articles = doc.select("article");
			if (articles.size() > 0) {
				nodes = new ArrayList<Node>();
				praseNode(nodes, articles.get(0));
			}
		}

		if (TextUtils.isEmpty(title)) {
			Object[] result = otherCase(doc.baseUri());
			if (result != null) {
				title = result[0].toString();
				time = (Date) result[1];
				nodes = (List<Node>) result[2];
			}
		}

		if (TextUtils.isEmpty(title)) {
			throw new RuntimeException("title is null,url=" + doc.baseUri());
		}
		if (time == null) {
			throw new RuntimeException("time is null,url=" + doc.baseUri());
		}
		if (nodes == null || nodes.size() == 0) {
			throw new RuntimeException("nodes is null,url=" + doc.baseUri());
		}

		newsDetial.title = title;
		newsDetial.publish_time = DateTimeUtil.parse(time, "yyyy-MM-dd HH:mm");
		newsDetial.nodes = nodes;
		return newsDetial;
	}

	private Object[] otherCase(String url) {
		Document doc = null;
		try {
			doc = SpiderPraser.spiderContentByJsoup(SpiderPraser.spiderByPhantomjs(url, "GBK"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (doc == null) {
			return null;
		}
		Elements boxes = doc.select("div.article-box");
		Elements titles;
		String title = null;
		Date time = null;
		List<Node> nodes = null;
		if (boxes.size() > 0) {
			titles = boxes.get(0).select("h1.article-title");
			if (titles.size() > 0) {
				title = getTrimContent(titles.get(0));
			}
			if (TextUtils.isEmpty(title)) {
				title = getTrimContent(boxes.get(0).child(0));
			}
		}

		Elements subs = doc.select("div.article-sub");

		if (subs.size() > 0) {
			Elements spans = subs.get(0).select("span");
			String timeStr = getTrimContent(spans.get(1));
			time = DateTimeUtil.parseTouTiaoPublishTime(timeStr);
			if (time == null) {
				timeStr = getTrimContent(spans.get(2));
				time = DateTimeUtil.parseTouTiaoPublishTime(timeStr);
			}
		}
		Elements articles = doc.select("div.article-content");
		if (articles.size() > 0) {
			nodes = new ArrayList<Node>();
			praseNode(nodes, articles.get(0));
		}

		if (!TextUtils.isEmpty(title) && time != null && (nodes != null && nodes.size() > 0)) {
			return new Object[] { title, time, nodes };
		} else {
			return null;
		}

	}

	private void praseNode(List<Node> list, Element e) {
		int childSize = e.children().size();
		String tagName = e.tagName();
		if (childSize > 0) {
			for (org.jsoup.nodes.Node node : e.childNodes()) {
				if (node instanceof Element) {
					praseNode(list, (Element) node);
				} else {
					Node n = addTxtNode(node.toString());
					if (n != null) {
						list.add(n);
					}
				}
			}
		} else {
			Node n;
			if (tagName.equals("img")) {
				n = addImgNode(e.attr("src"), baseUrl);
			} else {
				n = addTxtNode(e.text());
			}
			if (n != null) {
				list.add(n);
			}
		}
	}

	public static void main(String[] args) {

		//
		try {
			Document doc = SpiderPraser.spiderByJsoup("http://www.toutiao.com/a6451473291701010701/");
			new NewsDetialPraser().prase(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
