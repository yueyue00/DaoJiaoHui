package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 首页 亮点推荐 bean
 * 
 * @author zhangt
 * 
 */
public class HomePageInformListBean implements Serializable {
	
	@Expose
	public String type;
	@Expose
	public String date;
	@Expose
	public String title;
	@Expose
	public String content;

}
