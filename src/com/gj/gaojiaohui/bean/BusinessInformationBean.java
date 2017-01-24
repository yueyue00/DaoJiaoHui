package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 展商资料
 * @author Administrator
 *
 */
public class BusinessInformationBean {

	@Expose
	public int resultCode;
	/**展商介绍 */
	@Expose
	public String value;
	/**展商名称 */
	@Expose
	public String name;
	/**展商位置 */
	@Expose
	public String position;
	/**地图信息 */
	@Expose
	public String position_map;
	/**时间 */
	@Expose
	public String date;
	@Expose
	public List<BusinessInformationChildBean> list;
}
