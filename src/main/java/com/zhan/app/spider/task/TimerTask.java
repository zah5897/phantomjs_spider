package com.zhan.app.spider.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zhan.app.spider.spider.SpiderManager;

@Component
@EnableScheduling
public class TimerTask {

	//@Scheduled(cron = "0 0 0/1 * * ?") // 每小時
	@Scheduled(cron = "0 0/10 * * * ?") // 每5分钟执行一次
	public void spiderTast() {
		System.out.println("spiderTast");
		// 从0开始,每次100
		SpiderManager.getInstance().start();
	}
//	@Scheduled(cron = "0 0/5 * * * ?") // 每5分钟执行一次
//	public void bottleSpiderTast() {
//		System.out.println("spiderTast");
//		// 从0开始,每次100
//		SpiderManager.getInstance().bottleSpider();
//	}

	// @Scheduled(cron = "0/5 * * * * ? ") // 每5秒执行一次
	// public void test() {
	// System.out.println("timerPull");
	// System.out.println(Thread.currentThread().getName());
	// }
}