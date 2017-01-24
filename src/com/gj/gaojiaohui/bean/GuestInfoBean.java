package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

public class GuestInfoBean {
	@Expose
	public int resultCode;
	// 嘉宾姓名
	@Expose
	public String name;
	// 嘉宾头像
	@Expose
	public String pic;
	// 嘉宾id 融云用
	@Expose
	public String id;
	// 嘉宾职位
	@Expose
	public String position;
	// 公司名称
	@Expose
	public String company;
	// 嘉宾电话
	@Expose
	public String tel;
	// 嘉宾介绍
	@Expose
	public String value;
	// 邮箱
	@Expose
	public String email;
	// tag 0/嘉宾 1/服务人员
	@Expose
	public String tag;
	// 用户的token
	@Expose
	public String token;
}
