package com.zhan.app.spider.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

public class SpiderPraser {

	public  static  HtmlPage spiderByHtmlunit(String url)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getCookieManager().setCookiesEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setActiveXNative(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.waitForBackgroundJavaScript(600 * 1000);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());

		CookieManager CM = webClient.getCookieManager(); // WC = Your
															// WebClient's name
		Set<Cookie> cookies_ret = CM.getCookies();// 返回的Cookie在这里

		Iterator<Cookie> i = cookies_ret.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		
		
		WebRequest request=new WebRequest(new URL(url)); 
		request.setCharset("UTF-8");
		System.out.println(request.getCharset()); //
		final HtmlPage page = webClient.getPage(request);

		// 该方法在getPage()方法之后调用才能生效
		webClient.waitForBackgroundJavaScript(1000 * 3);
		webClient.setJavaScriptTimeout(0);
		webClient.close();
		return page;
	}

	public static  synchronized String spiderByPhantomjs(String url) throws IOException {
		return spiderByPhantomjs(url,"GBK");
	}

	public static  synchronized String spiderByPhantomjs(String url,String encode) throws IOException {
		String parentPath = getPhantomjsPath();
		Runtime rt = Runtime.getRuntime();
		Process p=null;
		if(SysUtil.isLinux()) {
			  p = rt.exec(parentPath + "phantomjs " + parentPath + "codes_encode.js " + url+" "+encode);
		}else {
			  p = rt.exec(parentPath + "phantomjs.exe " + parentPath + "codes_encode.js " + url+" "+encode);
		}
		
//		Process p = rt.exec(parentPath + "phantomjs.exe " + parentPath + "codes_encode.js " + url+" "+encode);
		InputStream is = p.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sbf = new StringBuffer();
		String tmp = "";
		while ((tmp = br.readLine()) != null) {
			sbf.append(tmp);
		}
		return sbf.toString();
	}
	
	
	public  static synchronized Document spiderByJsoup(String url) throws IOException {
		System.out.println("spiderByJsoup:"+url);
		Document doc = Jsoup.connect(url).get();
		return doc;
	}

	public static synchronized  Document spiderContentByJsoup(String content) throws IOException {
		Document doc = Jsoup.parse(content);
		return doc;
	}
	
	
	
	public static String getPhantomjsPath() {
	     if(SysUtil.isLinux()) {
	    	 return "/data/zah/soft/phantomjs-2.1.1-linux-x86_64/bin/";
	     }else {
	    	 return "D:\\soft\\phantomjs-2.1.1-windows\\bin\\";
	     }
	     
	}
}
