package com.zhan.app.spider.util;

public class SysUtil {
	public static boolean isLinux() {
		return System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0;
	}
}
 