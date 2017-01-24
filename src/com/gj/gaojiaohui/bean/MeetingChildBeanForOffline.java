package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 离线版 -会议列表的子布局实体类
 */
public class MeetingChildBeanForOffline {
	@Expose
	public int resultCode;
	/** 活动标题 */
	@Expose
	public String title;
	@Expose
	public String title_en;
	/** 1是室外 2是室内(这个是int值) */
	@Expose
	public int type;
	/** 室内导航的id,用于查询 */
	@Expose
	public String tag;
	/** 活动图片 */
	@Expose
	public String pic;
	/** 活动内容 */
	@Expose
	public String value;
	@Expose
	public String value_en;
	/** 开始时间 */
	@Expose
	public String start_date;
	/** 结束时间 */
	@Expose
	public String end_date;
	/** 活动id */
	@Expose
	public String id;
	/** 0,//未报名 1//审核中 2//报名成功 3//审核不通过 */
	@Expose
	public int apply;
	/** 会展地点 */
	@Expose
	public String site;
	@Expose
	public String site_en;
	/** 会展地址经度 */
	@Expose
	public String longitude;
	/** 会展地址纬度 */
	@Expose
	public String latitude;
	/** 展商介绍 */
	@Expose
	public String introduce;
}
