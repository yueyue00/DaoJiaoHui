package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 日程的时间滑块列表 实体类
 */
public class MeetingScheduleBean {
	/** 日程id */
	@Expose
	public String id;
	/** 星期几 */
	@Expose
	public String title;
	/** 会议时间,例: 11月5日 */
	@Expose
	public String time;
	/** 是否是上午 */
	@Expose
	public boolean now;
}
