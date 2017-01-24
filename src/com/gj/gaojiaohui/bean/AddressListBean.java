package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 通讯录 列表 - 实体类
 */
public class AddressListBean {
	@Expose
	public int resultCode;
	@Expose
	public List<AddressBean> list;
}
