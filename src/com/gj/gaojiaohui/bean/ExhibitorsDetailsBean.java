package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 展商详情--bean类
 * @author Administrator
 *
 */
public class ExhibitorsDetailsBean {
	@Expose
	public int resultCode;
	/** 展商id */
	@Expose
	public String id;
	/** 是否关注 */
	@Expose
	public boolean follow;
	/** 展商介绍 */
	@Expose
	public String value;
	/** 展商名称 */
	@Expose
	public String name;
	/** 展商位置 */
	@Expose
	public String position;
	/** 展位号 */
	@Expose
	public String position_map;
	/** 时间 */
	@Expose
	public String date;
	/** 联系方式 */
	@Expose
	public String tel;
	/** 展商公司行业 */
	@Expose
	public String lndustry;
	/** 展商公司地址 */
	@Expose
	public String address;
	/** 展商公司网址 */
	@Expose
	public String url;
	@Expose
	public List<ExhibitorsDetailsChildBean> list;
}
