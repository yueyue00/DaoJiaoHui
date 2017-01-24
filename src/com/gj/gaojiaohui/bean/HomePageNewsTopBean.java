package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class HomePageNewsTopBean implements Serializable{
	@Expose
	public String title;
	@Expose
	public String url;
	@Expose
	public String pic;
	@Expose
	public String value;

}
