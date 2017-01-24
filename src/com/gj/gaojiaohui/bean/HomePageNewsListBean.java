package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 首页 大会资讯 bean
 * 
 * @author zhangt
 * 
 */
public class HomePageNewsListBean implements Serializable {

	@Expose
	public String title;
	@Expose
	public String pic;
	@Expose
	public String value;
	@Expose
	public String url;

}
