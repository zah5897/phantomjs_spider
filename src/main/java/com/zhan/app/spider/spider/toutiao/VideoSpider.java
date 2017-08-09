package com.zhan.app.spider.spider.toutiao;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhan.app.common.Video;
import com.zhan.app.spider.service.NewsService;
import com.zhan.app.spider.util.SpiderPraser;
import com.zhan.app.spider.util.TextUtils;

public class VideoSpider {
	public static final String VIDEO_URL = "http://toutiao.com/subv_funny/";

	public void startSpider(NewsService newsService) {
		try {
			String htmlSrc = SpiderPraser.spiderByPhantomjs(VIDEO_URL);
			if (TextUtils.isNotEmpty(htmlSrc)) {
				Document doc = SpiderPraser.spiderContentByJsoup(htmlSrc);
				List<Video> videos = praseList(doc);
				for (Video v : videos) {

					v.video_url = "http://toutiao.com" + v.detail_html_url;
					v.detail_html_url = v.video_url;
					if (newsService != null) {
						boolean hasExist = newsService.hasExist(v);
						if (!hasExist) {
							newsService.insert(v);
						}

					}
				}
			}
			System.out.println("video spider over.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<Video> praseList(Document doc) {
		Elements liElements = doc.select("li[ga_event='video_item_click']");

		List<Video> videos = new ArrayList<Video>();
		if (liElements.size() > 0) {
			for (Element e : liElements) {
				Video v = praseItem(e);
				if (TextUtils.isEmpty(v.detail_html_url)) {
					continue;
				}
				videos.add(v);
			}
		}
		return videos;
	}

	private Video praseItem(Element e) {
		Elements elements = e.select("a.img-wrap");
		Video video = new Video();
		if (elements.size() > 0) {
			Element aEle = elements.first();
			String detailUrl = aEle.attr("href");
			Element imgEle = aEle.child(0);
			String thumbImgUrl = imgEle.attr("src");
			video.preview_url = thumbImgUrl;
			video.detail_html_url = detailUrl;
		}
		elements = e.select("div[ga_event=video_title_click]");
		if (elements.size() > 0) {
			Element titleA = elements.first();
			String title = titleA.text();
			video.title = title;
		}
		return video;
	}

	public static void main(String[] args) {
		new VideoSpider().startSpider(null);
	}
}
