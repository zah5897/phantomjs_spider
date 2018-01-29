package com.zhan.app.spider.util;

import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

public class MyJedisConnectionFactory extends JedisConnectionFactory {
	@Override
	public JedisConnection getConnection() {
		
		JedisShardInfo info=new JedisShardInfo("180.150.184.207","6379");
		info.setPassword("haoqi");
		Jedis jedis = new Jedis(info);
		JedisConnection connection = new JedisConnection(jedis, null, 0);
		connection.setConvertPipelineAndTxResults(true);
		return postProcessConnection(connection);
	}
}
