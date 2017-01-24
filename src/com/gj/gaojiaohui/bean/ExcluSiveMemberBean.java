package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;
/**
 * 专属服务人员-->嘉宾服务
 * @author lixiaoming
 *
 */
public class ExcluSiveMemberBean {
	/** 响应码 */
	@Expose
	public int resultCode;
	/** 头像 */
	@Expose
	public String pic;
	/** 服务人员姓名 */
	@Expose
	public String name;
	/** 联系电话 */
	@Expose
	public String tel;
	/** 服务人员类型 */
	@Expose
	public String position;
	/** 融云id */
	@Expose
	public String id;
}
