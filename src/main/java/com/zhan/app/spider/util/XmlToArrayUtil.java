package com.zhan.app.spider.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class XmlToArrayUtil {
	public static void main(String[] args) throws IOException {
		String path = "C:\\Users\\zah\\Desktop\\注册用户数.xml";

		InputStream in = new FileInputStream(new File(path));

		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

		String temp;
		StringBuilder sb = new StringBuilder();
		while ((temp = br.readLine()) != null) {
			sb.append(temp);
		}

		br.close();
		in.close();

		String txt = sb.toString();

		Document doc = SpiderPraser.spiderContentByJsoup(txt);
		Elements es=doc.select("Data[ss:Type=\"Number\"]");
		
		List<String> list=new ArrayList<>();
		for(Element e:es) {
			list.add(e.text());
		}
		System.out.println(Arrays.asList(list));
	}
}
