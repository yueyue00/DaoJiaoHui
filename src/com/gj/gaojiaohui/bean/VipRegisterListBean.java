package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 嘉宾签到列表实体类
 */
public class VipRegisterListBean {
	/** 嘉宾姓名 */
	@Expose
	public String name;
	/** 嘉宾id */
	@Expose
	public String vipId;
	/** 是否已签到 */
	@Expose
	public List<VipRegisterBtnBean> btn_list;
}
