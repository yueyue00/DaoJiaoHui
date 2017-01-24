package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 会议列表的子布局实体类
 */
public class MeetingChildBean {
	/** 会议id */
	@Expose
	public String meeting_id;
	/** 会议类型 0//会议 1//会展 2//活动 */
	@Expose
	public int type;
	/** 会议标题 */
	@Expose
	public String meeting_title;
	/** 会议主讲人 */
	@Expose
	public String meeting_compere;
	/** 时间地点 */
	@Expose
	public String meeting_site;
}
