package com.zhan.app.spider.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import com.zhan.app.common.node.Node;
import com.zhan.app.common.node.NodeType;
import com.zhan.app.spider.util.TextUtils;

public class BaseSpider {
	protected Node addTxtNode(String content) {
		String temp = content.trim().replace(Jsoup.parse("&nbsp;").text(), "");
		if (!TextUtils.isEmpty(temp)) {
			return new Node(NodeType.TXT.ordinal(), temp);
		}
		return null;
	}

	protected Node addImgNode(String url, String baseUrl) {
		if (!TextUtils.isEmpty(url)) {
			if (!url.startsWith("http")) {
				url = baseUrl + url;
			}
			return new Node(NodeType.IMG.ordinal(), url);
		}
		return null;
	}

	protected String getTrimContent(String content) {
		return content.trim().replace(Jsoup.parse("&nbsp;").text(), "");
	}

	protected String getTrimContent(Element element) {
		return element.text().trim().replace(Jsoup.parse("&nbsp;").text(), "");
	}
}
