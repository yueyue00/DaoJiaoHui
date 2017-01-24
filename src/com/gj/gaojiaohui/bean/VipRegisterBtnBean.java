package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 嘉宾签到实体的签到按钮实体类
 */
public class VipRegisterBtnBean {
	/** 签到图标名字 */
	@Expose
	public String name;
	/** 签到图标id */
	@Expose
	public String btn_id;
	/** 是否已签到 */
	@Expose
	public boolean state;
}
