package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 离线展商详情 - 实体类
 */
public class ExhibitorsBeanForOffline implements Serializable{
	/** 展商id */
	@Expose
	public String id;
	/** 展商名称 */
	@Expose
	public String name;
	@Expose
	public String name_en;
	/** 展商内容 */
	@Expose
	public String value;
	@Expose
	public String value_en;
	/** 展位号*/
	@Expose
	public String position;
	/** 电话*/
	@Expose
	public String tel;
	/** 展商公司行业 */
	@Expose
	public String lndustry;
	@Expose
	public String lndustry_en;
	/** 展商公司地址 */
	@Expose
	public String address;
	@Expose
	public String address_en;
	/** 展商公司网址 */
	@Expose
	public String url;

}
