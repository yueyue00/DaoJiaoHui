package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;


/**
 * 留言详情--子bean类
 * @author Administrator
 *
 */
public class MessageDetailsChildBean {
	/**留言标题 */
	@Expose
	public String title;
	/**留言内容 */
	@Expose
	public String value;
	/**留言人姓名 */
	@Expose
	public String name;
	/**留言时间 */
	@Expose
	public String date;
}
