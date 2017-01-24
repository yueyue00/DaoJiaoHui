package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 我关注的 - 实体类
 */
public class FollowBean {
	/** 展商名称 */
	@Expose
	public String name;
	/** 展商介绍 */
	@Expose
	public String value;
	/** 展商id */
	@Expose
	public String id;
}
