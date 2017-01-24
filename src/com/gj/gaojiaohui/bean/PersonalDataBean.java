package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 个人资料--bean
 * @author Administrator
 *
 */
public class PersonalDataBean {

	@Expose
	public int resultCode;
	/**用户姓名 */
	@Expose
	public String name;
	/**联系方式 */
	@Expose
	public String tel;
	/**邮箱 */
	@Expose
	public String email;
	/**职位 */
	@Expose
	public String position;
	/**所属单位 */
	@Expose
	public String company;
	@Expose
	public String token;
}
