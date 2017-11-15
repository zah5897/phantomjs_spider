package com.zhan.app.spider.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	public static String getYearMonthDay(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		return year + "_" + month + "_" + day;
	}

	public static String parse(Date result) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(result);
	}

	public static String parse(Date result, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(result);
	}

	public static String parseBirthday(Date result) {
		if (result == null) {
			return new String();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(result);
	}

	public static String getTodayStr() {
		return parseBirthday(new Date());
	}

	public static String getAge(Date birthday) {
		if (birthday == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(birthday);
		int birthdayYear = c.get(Calendar.YEAR);
		c.setTimeInMillis(System.currentTimeMillis());
		int nowYear = c.get(Calendar.YEAR);

		if (birthdayYear > nowYear) {
			return null;
		}
		return String.valueOf(nowYear - birthdayYear);
	}

	public static Date parseDate(String strDate) {
		if (TextUtils.isEmpty(strDate)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Date parseTouTiaoPublishTime(String strDate) {
		if (TextUtils.isEmpty(strDate)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			System.err.println("time format error:strDate="+strDate);
		}
		return null;
	}

	public static Date parseBaiduPublishTime(String strDate) {
		if (TextUtils.isEmpty(strDate)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getYear() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return String.valueOf(c.get(Calendar.YEAR));
	}

	// public static void main(String[] args) throws ParseException {
	// Calendar c = Calendar.getInstance();
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// Date birthday = sdf.parse("2017-03-08 00:00:00");
	// String age = getAge(birthday);
	// System.out.println(age);
	// }
}
