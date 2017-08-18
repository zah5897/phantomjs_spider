package com.zhan.app.spider.spider.toutiao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhan.app.common.News;
import com.zhan.app.spider.spider.BaseSpider;
import com.zhan.app.spider.util.TextUtils;

public class NewsSimplePraser extends BaseSpider {
	private static Logger log = Logger.getLogger(NewsSimplePraser.class);

	public List<News> prase(Document doc) throws IOException {
		List<News> newsList = new ArrayList<News>();
		Elements wcommonFeed = doc.select("div.wcommonFeed");
		Elements newsLis = null;
		if (wcommonFeed.size() > 0) {
			Elements uls = wcommonFeed.get(0).select("ul");
			if (uls.size() > 0) {
				newsLis = uls.get(0).select("li");
			}
		}

		if (newsLis != null && newsLis.size() > 0) {
			int size = newsLis.size();
			for (int i = 0; i < size; i++) {
				try {
					newsList.add(itemToNews(newsLis.get(i)));
				} catch (Exception e) {
					continue;
				}
			}
		} else {
			throw new RuntimeException("Document not item url=" + doc.baseUri());
		}
		return newsList;
	}

	private News itemToNews(Element newsItem) throws UnsupportedEncodingException {
		String[] imageAndUrl = getImage(newsItem);
		if (imageAndUrl == null) {
			throw new RuntimeException("img and url is null\n");
		}

		String title = getTitle(newsItem);

		String abstractContent = getAbstract(newsItem);

		if (TextUtils.isEmpty(abstractContent)) {
			abstractContent = title;
		}

		String from = getFrom(newsItem);
		News news = new News();
		news.icon = imageAndUrl[0];
		if (imageAndUrl[1].startsWith("http")) {
			news.url = imageAndUrl[1];
			if(news.url.startsWith("http://")){
				news.url="https"+news.url.substring(news.url.indexOf(":"));
			}
		} else {
			news.url = "https://toutiao.com" + imageAndUrl[1];
		}

		if (news.url.startsWith("https://")) {
			news.url_type = 1;
		}
		news.title = title;
		news.news_abstract = abstractContent;
		news.from = from;
		return news;

	}

	// image and url
	private String[] getImage(Element newsItem) {
		Elements left_image = newsItem.select("a[class='img-wrap'] img");
		Elements group_ele = newsItem.select("a[class='img-wrap']");
		String groupId = null;
		if (group_ele.size() > 0) {
			groupId = group_ele.get(0).attr("href");
		}
		String image = null;

		if (left_image.size() > 0) {
			Element box_left = left_image.get(0);
			image = box_left.attr("src");
		}

		if (!TextUtils.isEmpty(image)) {
			if (!image.startsWith("http")) {
				image = "http:" + image;
			}
		}

		if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(image)) {
			throw new RuntimeException("groupId is null or image is null");
		}
		return new String[] { image, groupId };

	}

	private String getTitle(Element newsItem) {
		Elements titles = newsItem.select("a[class='link title']");
		String title = null;

		if (titles.size() > 0) {
			title = titles.first().text();
		}

		if (TextUtils.isEmpty(title)) {
			throw new RuntimeException(" title is null");
		}
		return title;
	}

	private String getAbstract(Element newsItem) {
		// Elements rbox = newsItem.select("div.rbox");
		//
		// if (rbox == null || rbox.size() == 0) {
		// System.out.println("getAbstract is null");
		// return null;
		// }
		// Elements abstractDivs = rbox.select("div.abstract");
		// String abstractContent = null;
		// if (abstractDivs != null && abstractDivs.size() > 0) {
		// Elements as = abstractDivs.get(0).select("a");
		// if (as != null && as.size() > 0) {
		// abstractContent = as.get(0).text();
		// } else {
		// abstractContent = abstractDivs.get(0).getAllElements().get(0).text();
		// }
		// }
		// if (TextUtils.isEmpty(abstractContent)) {
		// System.out.println("abstractContent is null");
		// }
		return null;
	}

	public String getFrom(Element newsItem) {
		Elements sources = newsItem.select("a[class='lbtn source']");
		String source = "";
		if (sources.size() > 0) {
			source = sources.get(0).text();
		}
		source = getTrimContent(source);
		source = source.replace("?", "");
		return source;
	}
}
