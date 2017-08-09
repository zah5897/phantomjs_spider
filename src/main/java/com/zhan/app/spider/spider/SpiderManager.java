package com.zhan.app.spider.spider;

import com.zhan.app.spider.spider.baidu.SpiderBaiDu;
import com.zhan.app.spider.spider.toutiao.SpiderTouTiao;

public class SpiderManager {
	private static SpiderManager spiderManager;

	private SpiderTouTiao spiderTouTiao;

	private SpiderBaiDu spiderBaiDu;

	private SpiderManager() {
	}

	public static SpiderManager getInstance() {
		if (spiderManager == null) {
			spiderManager = new SpiderManager();
		}
		return spiderManager;
	}

	public SpiderTouTiao getTouTiaoSpider() {
		if (spiderTouTiao == null) {
			spiderTouTiao = new SpiderTouTiao();
		}
		return spiderTouTiao;
	}

	private SpiderBaiDu getBaiduSpider() {
		if (spiderBaiDu == null) {
			spiderBaiDu = new SpiderBaiDu();
		}
		return spiderBaiDu;
	}

	public void start() {
		getTouTiaoSpider().start();
		// getTouTiaoSpider().startVideoSpider();
		getBaiduSpider().start();
	}

	public void testBaidu() {
		getBaiduSpider().start();
	}

	public static void main(String[] args) {
		SpiderManager.getInstance().start();
	}
}