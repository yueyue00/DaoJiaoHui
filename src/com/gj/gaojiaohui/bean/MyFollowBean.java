package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 关注我的 - 实体类
 */
public class MyFollowBean {
	/** 用户姓名 */
	@Expose
	public String name;
	/** 用户星级 */
	@Expose
	public int star;
	/** 用户id */
	@Expose
	public String id;
}
