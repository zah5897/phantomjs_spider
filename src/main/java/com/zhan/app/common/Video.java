package com.zhan.app.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "spider_brower_app_video")
public class Video {
	@Id
	public String id;
	public String preview_url;
	public String preview_large_url;
	public String video_url;
	public String title;
	public String detail_html_url;
	public int browse_count;
}
