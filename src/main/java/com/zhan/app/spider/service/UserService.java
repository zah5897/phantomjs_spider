package com.zhan.app.spider.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhan.app.common.User;
import com.zhan.app.spider.dao.UserDao;

@Service
public class UserService {
	@Resource
	private UserDao userDao;

	public long count(User user) {
		return userDao.countByDeviceAndToken(user.getDeviceId(), user.getToken());
	}

	public List<User> getPushUser(String last_id, int count) {
		return userDao.getPushUser(last_id, count);
	}

	public List<User> getUser() {
		return userDao.getUsers();
	}
	public long getZH_CNUserCount(String collectionName) {
		return userDao.getZH_CNUserCount(collectionName);
	}
	public long getZH_CNUserCount(String collectionName,String aid) {
		return userDao.getZH_CNUserCount(collectionName,aid);
	}
	public List<User> getZH_CNUser(String collectionName,String aid,int skip,int limit){
		return userDao.getZH_CNUser(collectionName,aid, skip, limit);
	}
	public List<User> getZH_CNUser(String collectionName,int skip,int limit){
		return userDao.getZH_CNUser(collectionName, skip, limit);
	}
	public List<User> getZH_CNUser(String collectionName) {
		return userDao.getZH_CNUser(collectionName);
	}
	public List<User> getZH_CNUser(String collectionName,String aid) {
		return userDao.getZH_CNUser(collectionName,aid);
	}
	public List<User> getZH_CNDaoHangUser() {
		return userDao.getZH_CNDaoHangUser();
	}
	public List<User> getZH_CNMobleBrowserUser() {
		return userDao.getZH_CNMobileBrowserUser();
	}

}
