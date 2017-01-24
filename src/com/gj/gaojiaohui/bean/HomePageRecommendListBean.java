package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 首页 大会通知列表 bean
 * 
 * @author zhangt
 * 
 */
public class HomePageRecommendListBean implements Serializable {

	@Expose
	public String news_id;
	@Expose
	public String news_title;
	@Expose
	public String news_pic;

}
