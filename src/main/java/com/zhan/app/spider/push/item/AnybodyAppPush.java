package com.zhan.app.spider.push.item;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.zhan.app.common.News;
import com.zhan.app.common.User;
import com.zhan.app.spider.util.TextUtils;

public class AnybodyAppPush extends BaseAppPush {

	public AnybodyAppPush(String keyStoryName,String aid, String connectionName) {
		super(keyStoryName,aid, connectionName);
	}

	@Override
	public void push(News news) {
		List<User> errorUser = new ArrayList<User>();
		long count;
		if (TextUtils.isEmpty(aid)) {
			count = getUserService().getZH_CNUserCount(connectionName);
		} else {
			count = getUserService().getZH_CNUserCount(connectionName, aid);
		}
		int pageSize = 1000;
		long pageNumber = count / 1000;
		if(pageNumber<1){
			pageNumber=1;
		}
		for (int i = 0; i < pageNumber; i++) {
			int skip = i * pageSize;
			List<User> users;
			if (TextUtils.isEmpty(aid)) {
				users = (List<User>) getUserService().getZH_CNUser(connectionName, skip, pageSize);
			} else {
				users = (List<User>) getUserService().getZH_CNUser(connectionName, aid, skip, pageSize);
			}
			JSONObject object = createJson(news);
			if (users != null && users.size() > 0) {
				for (User user : users) {
					 setToken(object, user);
					try {
						leftPush(object.toJSONString());
					} catch (Exception e) {
						System.err.println("left push:" + e.getMessage());
						user.app_name = keyStoryName;
						errorUser.add(user);
					}
				}
			}
			errorPush(errorUser, object);
		}
		whileCount = 0;
	}
}
