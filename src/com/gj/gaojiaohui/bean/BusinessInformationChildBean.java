package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 展商资料--子bean
 * @author Administrator
 *
 */
public class BusinessInformationChildBean {

	/**展商标题 */
	@Expose
	public String title;
	/**内容 */
	@Expose
	public String value;
	/**展品详情 id */
	@Expose
	public String id;
	/**展商图片 */
	@Expose
	public String pic;
}
