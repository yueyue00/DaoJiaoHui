package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 会展详情
 * @author Administrator
 *
 */
public class HuiZhanDetailsBean {

	@Expose
	public int resultCode;
	/**页面标题*/
	@Expose
	public String page_title;
	/**会议标题*/
	@Expose
	public String meeting_title;
	/**会议内容*/
	@Expose
	public String meeting_value;
	/** 是否参加 */
	@Expose
	public boolean meeting_join;
	/**活动时间*/
	@Expose
	public String meeting_date;
	/**活动地点*/
	@Expose
	public String meeting_site;
	/**地图信息*/
	@Expose
	public String meeting_map;
	/**会场经度*/
	@Expose
	public String longitude;
	/**会场纬度*/
	@Expose
	public String latitude;
	@Expose
	public List<HuiZhanDetailsChildBean> vip_list;
}
