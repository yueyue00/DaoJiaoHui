package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 会议详情
 * 
 * @author Administrator
 * 
 */
public class MettingDetailsBean {

	@Expose
	public int resultCode;
	/** 页面标题 */
	@Expose
	public String page_title;
	/** 会议标题 */
	@Expose
	public String meeting_title;
	/** 会议内容 */
	@Expose
	public String meeting_value;
	/** 是否参加 */
	@Expose
	public boolean meeting_join;
	/** 活动开始时间 */
	@Expose
	public String start_date;
	/** 活动结束时间 */
	@Expose
	public String end_date;
	/** 活动地点 */
	@Expose
	public String meeting_site;
	/** 室内导航的id 用于查询 */
	@Expose
	public String tag;
	/** 1/室外 2/室内 */
	@Expose
	public int type;
	/** 会场经度 */
	@Expose
	public String longitude;
	/** 会场纬度 */
	@Expose
	public String latitude;
}
