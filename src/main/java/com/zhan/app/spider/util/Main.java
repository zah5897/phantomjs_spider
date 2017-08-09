package com.zhan.app.spider.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;

public class Main {

	public static void main(String[] args) {
		try {
			Document doc  = Jsoup.parse(new File("C:/Users/zah/Desktop/arrays.xml"),"utf-8");
		    Elements eles=	doc.select("string-array[name='province']").first().children();
			System.out.println(eles.size());
			List<City> province=new ArrayList<City>();
			int id=1;
			for(Element e:eles){
				String name=e.text();
				City c=new City();
				c.setId(id);
				c.setName(name);
				province.add(c);
				id++;
			}
			 Elements cities =	doc.select("string-array[name='city']").first().children();
			
			 int index=0;
			 for(Element e:cities){
				 String name=e.text();
                 String[] city=name.split(",");
                 List<City> cs=new ArrayList<City>();
                 for(int i=0;i<city.length;i++){
                	City c=new City();
     				c.setId(id);
     				c.setName(city[i]);
     				c.setParent_id(province.get(index).getId());
     				cs.add(c);
     				province.get(index).setChildren(cs);
     				id++;
                 }
                 index++;
			 }
			System.out.println(JSON.toJSON(province));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
class City{
	private int id;
	private String name;
	private List<City> children;
	private int parent_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<City> getChildren() {
		return children;
	}
	public void setChildren(List<City> children) {
		this.children = children;
	}
	public int getParent_id() {
		return parent_id;
	}
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	
	
}