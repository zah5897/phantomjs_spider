package com.zhan.app.spider.util;

import com.zhan.app.common.News;

public class NotNullUtil {
	public static boolean canPrase(News news) {
		if (news == null || TextUtils.isEmpty(news.url) || TextUtils.isEmpty(news.news_abstract)
				|| TextUtils.isEmpty(news.title) || TextUtils.isEmpty(news.icon)) {
			return false;
		}
		return true;
	}
}
