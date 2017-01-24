package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 活动列表--bean类
 * @author Administrator
 *
 */
public class ActivityListBean {
	@Expose
	public int resultCode;
	@Expose
	public List<ActivityListChildBean> list;
}
