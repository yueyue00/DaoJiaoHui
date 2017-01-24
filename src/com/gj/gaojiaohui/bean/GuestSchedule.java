package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

public class GuestSchedule {
	// 行程标题 例: 抵达深圳
	@Expose
	public String name;
	// 完成时间
	@Expose
	public String date;
	// 是否已完成
	@Expose
	public String state;
}
