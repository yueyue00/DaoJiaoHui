package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 展商列表--子bean类
 * @author Administrator
 *
 */
public class ExhibitorsListChildBean {
	/**标题 */
	@Expose
	public String title;
	/**内容 */
	@Expose
	public String value;
	/**展商id */
	@Expose
	public String id;
	/**是否关注 */
	@Expose
	public boolean follow;
	/** 展商公司行业 */
	@Expose
	public String lndustry;
	/** 展商公司地址 */
	@Expose
	public String address;
	/** 展商公司网址 */
	@Expose
	public String url;
}
