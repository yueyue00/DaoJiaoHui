package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

public class ServiceFeedbackDetailBean {
	/** 响应码 */
	@Expose
	public int resultCode;
	@Expose
	public List<ServiceFeedbackDetailChildBean> list;
}
