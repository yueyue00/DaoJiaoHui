package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 关注我的 - 实体类
 */
public class FollowMyBean {
	/** 用户姓名 */
	@Expose
	public String name;
	/** 是否被我关注 */
	@Expose
	public boolean follow;
	/** 用户id */
	@Expose
	public String id;
}
