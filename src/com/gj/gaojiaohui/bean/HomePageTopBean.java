package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 首页 顶部头图
 * 
 * @author zhangt
 * 
 */
public class HomePageTopBean implements Serializable{
	
	@Expose
	public String title;
	
	@Expose
	public String pic;
	
	@Expose
	public String url;

}
