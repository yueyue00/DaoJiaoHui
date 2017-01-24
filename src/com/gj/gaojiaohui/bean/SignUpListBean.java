package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 报名列表
 * @author Administrator
 *
 */
public class SignUpListBean {

	@Expose
	public int resultCode;
	@Expose
	public List<SignUpListChildBean> list;
}
