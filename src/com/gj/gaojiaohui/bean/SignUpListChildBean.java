package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 报名列表--子bean
 * @author Administrator
 *
 */
public class SignUpListChildBean {

	/**活动标题 */
	@Expose
	public String title;
	/**活动图片url */
	@Expose
	public String pic;
	/**活动id */
	@Expose
	public int id;
	/**活动内容 */
	@Expose
	public String value;
	/**活动时间 */
	@Expose
	public String date;
	/**0//审核中 1//审核通过 2//审核不通过 3//已取消*/
	@Expose
	public int join_state;
}
