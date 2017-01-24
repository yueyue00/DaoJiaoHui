package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 会议列表的父布局(标题)实体类
 */
public class MeetingGroupBean {
	/** 时间 */
	@Expose
	public String date;
	/** 是否是上午 */
	@Expose
	public boolean forenoon;
	/** 会议列表数据 */
	@Expose
	public List<MeetingChildBean> list;
}
