package com.zhan.app.spider.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.zhan.app.common.User;
import com.zhan.app.spider.util.TextUtils;

@Repository("userDao")
public class UserDao extends BaseDao {
	@Resource
	protected MongoTemplate mongoTemplate;

	public String save(User user) {
		mongoTemplate.save(user);
		return user.getId();
	}

	public long countByDeviceAndToken(String device_id, String token) {
		Query query = new Query();
		Criteria criteria = Criteria.where("deviceId").is(device_id).and("token").is(token);
		query.addCriteria(criteria);
		return mongoTemplate.count(query, User.class);
	}

	public List<User> getPushUser(String last_id, int count) {
		Query query = new Query();
		// query.with(new Sort(new Order(Direction.ASC, "_id")));
		query.limit(count);
		if (!TextUtils.isEmpty(last_id)) {
			Criteria criteria = Criteria.where("_id").gt(last_id);
			query.addCriteria(criteria);
		}
		return mongoTemplate.find(query, User.class);
	}

	public List<User> getUsers() {
		return mongoTemplate.findAll(User.class);
	}
	public List<User> getZH_CNUser(String collectionName,String aid,int skip,int limit) {
		Query query = new Query();
		Criteria criteria = Criteria.where("zh_cn").is("1").and("aid").is(aid);
		query.with(new Sort(Direction.DESC,"_id"));
		query.skip(skip);
		query.limit(limit);
		query.addCriteria(criteria);
		return mongoTemplate.find(query, User.class, collectionName);

	}
	public List<User> getZH_CNUser(String collectionName,int skip,int limit) {
		Query query = new Query();
		Criteria criteria = Criteria.where("zh_cn").is("1");
		query.with(new Sort(Direction.DESC,"_id"));
		query.skip(skip);
		query.limit(limit);
		query.addCriteria(criteria);
		return mongoTemplate.find(query, User.class, collectionName);
		
	}
	public long getZH_CNUserCount(String collectionName) {
		Query query = new Query();
		Criteria criteria = Criteria.where("zh_cn").is("1");
		query.addCriteria(criteria);
		
		return mongoTemplate.count(query, User.class, collectionName);

	}
	public long getZH_CNUserCount(String collectionName,String aid) {
		Query query = new Query();
		Criteria criteria = Criteria.where("zh_cn").is("1").and("aid").is(aid);
		query.addCriteria(criteria);
		
		return mongoTemplate.count(query, User.class, collectionName);
		
	}
	public List<User> getZH_CNUser(String collectionName) {
		Query query = new Query();
		Criteria criteria = Criteria.where("zh_cn").is("1");
		query.addCriteria(criteria);
		return mongoTemplate.find(query, User.class, collectionName);

	}
	public List<User> getZH_CNUser(String collectionName,String aid) {
		Query query = new Query();
		Criteria criteria = Criteria.where("zh_cn").is("1").and("aid").is(aid);
		query.addCriteria(criteria);
		return mongoTemplate.find(query, User.class, collectionName);
		
	}
	public List<User> getZH_CNDaoHangUser() {
		Query query = new Query();
		Criteria criteria = Criteria.where("zh_cn").is("1");
		query.addCriteria(criteria);
		return mongoTemplate.find(query, User.class,"push_daohang_user");
		
	}
	public List<User> getZH_CNMobileBrowserUser() {
		Query query = new Query();
		Criteria criteria = Criteria.where("zh_cn").is("1");
		query.addCriteria(criteria);
		return mongoTemplate.find(query, User.class,"push_mobile_browser_user");
	}

}
