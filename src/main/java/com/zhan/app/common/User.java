package com.zhan.app.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.annotation.JSONField;

@Document(collection = "push_news_user")
public class User {
	@Id
	private String id;
	private String deviceId;
	private String token;
	@JSONField(serialize = false)
	private int state;
	@JSONField(serialize = false)
	private int test;
	
	
	private String zh_cn;
	

	private long create_time;
	
	
	private String aid;
	
	
	public String app_name;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getZh_cn() {
		return zh_cn;
	}

	public void setZh_cn(String zh_cn) {
		this.zh_cn = zh_cn;
	}

	public int getTest() {
		return test;
	}

	public void setTest(int test) {
		this.test = test;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	
}
