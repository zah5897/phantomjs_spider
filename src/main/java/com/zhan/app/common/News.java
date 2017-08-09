package com.zhan.app.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "spider_news_simple")
public class News {
	@Id
	public String id;
	public String title;
	public String icon;
	public String news_abstract;
	public String url;
	public String from;
	public String publish_time;
	public int type=0; //0 为头条，1为百度
}
