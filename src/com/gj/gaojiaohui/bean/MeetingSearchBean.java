package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 日程搜索实体类
 */
public class MeetingSearchBean {
	/** 响应码 */
	@Expose
	public int resultCode;
	/** 会议列表数据 */
	@Expose
	public List<MeetingChildBean> schedule_list;
}
