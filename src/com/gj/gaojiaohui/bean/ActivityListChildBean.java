package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 活动列表--子bean类
 * @author Administrator
 *
 */
public class ActivityListChildBean {

	/**活动标题 */
	@Expose
	public String title;
	/**活动内容 */
	@Expose
	public String value;
	/**活动id */
	@Expose
	public int id;
	/**活动图片 */
	@Expose
	public String pic;
	/**活动时间 */
	@Expose
	public String date;
}
