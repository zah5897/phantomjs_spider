package com.zhan.app.spider.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.zhan.app.common.News;
import com.zhan.app.common.NewsDetial;
import com.zhan.app.common.Video;

@Repository("newsDao")
public class NewsDao extends BaseDao {
	@Resource
	protected MongoTemplate mongoTemplate;

	public String save(News news) {
		mongoTemplate.save(news);
		return news.id;
	}

	public String save(Video video) {
		mongoTemplate.save(video);
		return video.id;
	}

	public void save(NewsDetial news) {
		mongoTemplate.save(news);
	}

	public boolean hasExist(News news) {
		Query query = new Query();
		Criteria criteria = Criteria.where("url").is(news.url);
		query.addCriteria(criteria);
		long count = mongoTemplate.count(query, News.class);
		return count > 0;
	}

	public long getCount(Video video) {
		Query query = new Query();
		Criteria criteria = Criteria.where("title").is(video.title);
		query.addCriteria(criteria);
		return mongoTemplate.count(query, Video.class);
	}

	public List<?> list(String time, int limit) {
		Query query = new Query();
		query.limit(limit);
		return mongoTemplate.find(query, News.class);
	}

	public void deleteByTitle(String title) {
		Query query = new Query();
		Criteria criteria = Criteria.where("title").is(title);
		query.addCriteria(criteria);
		mongoTemplate.remove(query, News.class);
		mongoTemplate.remove(query, NewsDetial.class);
	}

	public void deleteByUrl(String url) {
		Query query = new Query();
		Criteria criteria = Criteria.where("url").is(url);
		query.addCriteria(criteria);
		mongoTemplate.remove(query, News.class);
		mongoTemplate.remove(query, NewsDetial.class);
	}

	public List<News> getNewsByUrl(String url) {
		Query query = new Query();
		Criteria criteria = Criteria.where("url").is(url);
		query.addCriteria(criteria);
		return mongoTemplate.find(query, News.class);
	}

	public List<News> getNewsByID(String id) {
		Query query = new Query();
		Criteria criteria = Criteria.where("_id").is(id);
		query.addCriteria(criteria);
		return mongoTemplate.find(query, News.class);
	}

	public void deleteByID(String id) {
		Query query = new Query();
		Criteria criteria = Criteria.where("_id").is(id);
		query.addCriteria(criteria);
		mongoTemplate.remove(query, News.class);
		mongoTemplate.remove(query, NewsDetial.class);

	}

}
