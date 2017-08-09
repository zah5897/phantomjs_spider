package com.zhan.app.spider.push;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.zhan.app.common.News;
import com.zhan.app.spider.push.item.AnybodyAppPush;
import com.zhan.app.spider.push.item.BaseAppPush;
import com.zhan.app.spider.push.item.NewsAppPush;

public class PushManager {
	private static PushManager pushManager;
	private int index = 0;
	private List<BaseAppPush> pushItems;

	public static PushManager getInstance() {
		if (pushManager == null) {
			pushManager = new PushManager();
			pushManager.initPushItems();
		}
		return pushManager;
	}

	private void initPushItems() {
		pushItems = new ArrayList<BaseAppPush>();
		// 新版手机找人
		pushItems.add(new AnybodyAppPush("AW_findGps_1225814802", "1225814802", "push_news_user"));
		pushItems.add(new AnybodyAppPush("mp_findGps_1236362107", "1236362107", "push_news_user"));
		// 旧版新闻推送
		pushItems.add(new NewsAppPush("AW_FindU_843144601", "843144601", "push_news_user"));

		// 其他
		pushItems.add(new NewsAppPush("news_look_for", null, "push_news_user_look_for"));
		pushItems.add(new NewsAppPush("news_dao_hang", null, "push_daohang_user"));
		pushItems.add(new NewsAppPush("news_mobile_browser", null, "push_mobile_browser_user"));
		pushItems.add(new NewsAppPush("mp_findu", null, "push_findu"));
		index = new Random().nextInt(pushItems.size());
	}

	public void extraPush(News toPush) {
		if (index >= 0 && index < pushItems.size()) {
			BaseAppPush pushItem=pushItems.get(index);
			pushItem.push(toPush);
			index++;
		} else {
			index = 0;
			extraPush(toPush);
		}
	}
}