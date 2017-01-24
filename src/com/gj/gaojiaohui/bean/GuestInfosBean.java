package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

public class GuestInfosBean {
	@Expose
	public String resultCode="500";
	// 是否显示行程
	@Expose
	public String Permission;
	@Expose
	public GuestInfoBean data;
	@Expose
	public List<GuestSchedule> list;

}
