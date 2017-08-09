package com.zhan.app.spider.push.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSONObject;
import com.zhan.app.common.News;
import com.zhan.app.common.User;
import com.zhan.app.spider.service.UserService;
import com.zhan.app.spider.util.RedisKeys;
import com.zhan.app.spider.util.SpringContextUtil;
import com.zhan.app.spider.util.TextUtils;

public class BaseAppPush {
	
   protected String keyStoryName;
   protected String connectionName;
   protected String aid;
   protected long lastPushTime = 0;
   public static final int MIX_TIME_EVERY=45;
   
   protected int  whileCount = 0;
   
   
   public BaseAppPush(String keyStoryName,String aid,String connectionName){
	    this.keyStoryName=keyStoryName;
	    this.connectionName=connectionName;
	    this.aid=aid;
   }
   
   public void push(News news){
   }
   
   protected boolean canPush(){
	   return ((System.currentTimeMillis()/1000/60)-lastPushTime)>MIX_TIME_EVERY;
   }
   
   public List<User> getAllUer(){
	  return null;   
   }
   protected void leftPush(String txt) {
		getRedisTemplate().opsForList().leftPush(RedisKeys.KEY_NEWS_PUSH, txt);
	}
   private RedisTemplate<String, String> getRedisTemplate() {
		return SpringContextUtil.getBean("redisTemplate");
   }
   protected UserService getUserService() {
		return SpringContextUtil.getBean("userService");
   }
   
   protected JSONObject createJson(News news){
	   JSONObject object = new JSONObject();
		String title = news.title;
		object.put("id", news.id);
		object.put("alert", title);
		object.put("app_name", keyStoryName);
		return object;
   }

   protected void setToken(JSONObject object,User user){
	   object.put("token", user.getToken());
	   object.put("time", getPushTime()); // 精度分钟
   }
   protected long getPushTime() {
		return System.currentTimeMillis();
   }
   
   protected void errorPush(List<User> errorUsers, JSONObject newsObj) {
		whileCount++;
		if (errorUsers.size() == 0 || newsObj == null) {
			return;
		}
		List<User> tempErrorUsers = new ArrayList<User>();

		for (User user : errorUsers) {

			if (TextUtils.isEmpty(user.app_name)) {
				continue;
			}
			newsObj.put("app_name", user.app_name);
			newsObj.put("token", user.getToken());
			newsObj.put("time", getPushTime()); // 精度分钟
			try {
				leftPush(newsObj.toJSONString());
			} catch (Exception e) {
				System.err.println("left push:" + e.getMessage());
				tempErrorUsers.add(user);
			}
		}
		if (whileCount < 5 && tempErrorUsers.size() > 0) {
			errorPush(tempErrorUsers, newsObj);
		}
	}
}
