package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 关注我的 列表 - 实体类
 */
public class FollowMyListBean {
	@Expose
	public int resultCode;
	@Expose
	public List<FollowMyBean> list;
}
