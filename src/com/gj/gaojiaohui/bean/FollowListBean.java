package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 我关注的 列表 - 实体类
 */
public class FollowListBean {
	@Expose
	public int resultCode;
	@Expose
	public List<FollowBean> list;
}
