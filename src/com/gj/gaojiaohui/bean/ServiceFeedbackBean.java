package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

public class ServiceFeedbackBean {
	@Expose
	public int resultCode;
	@Expose
	public List<ServiceFeedbackChildBean> list;
}
