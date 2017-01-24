package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

public class UserNameLoginBean {
	@Expose
	public String resultCode = "500";
	@Expose
	public String message ;
	// 用户id
	@Expose
	public String user_id;
	// 用户名称
	@Expose
	public String name;
	// 用户权限级别
	@Expose
	public String user_permission;
	// 用户token
	@Expose
	public String token;
}
