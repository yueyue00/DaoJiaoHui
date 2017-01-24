package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 日程实体类
 * 
 */
public class MeetingScheduleListBean {
	/** 响应码 */
	@Expose
	public int resultCode;
	/** 顶部日程滑块对应的数据 */
	@Expose
	public List<MeetingScheduleBean> date_list;
	/** 底部日程对应的会议列表数据 */
	@Expose
	public List<MeetingGroupBean> schedule_list;
}
