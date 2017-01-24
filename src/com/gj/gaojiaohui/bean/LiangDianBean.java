package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 亮点的bean
 * 
 * @author zhangt
 * 
 */
public class LiangDianBean implements Serializable {
	@Expose
	public String id;
	@Expose
	public String title;
	@Expose
	public String pic;
	@Expose
	public String value;
}
