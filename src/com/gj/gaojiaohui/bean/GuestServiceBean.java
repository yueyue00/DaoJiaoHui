package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;
/**
 * 嘉宾服务的bean类
 * @author lixiaoming
 *
 */
public class GuestServiceBean {
	/** 响应码 */
	@Expose
	public int resultCode;
	/** 服务人员电话 */
	@Expose
	public String tel;
	/** 酒店地图 */
	@Expose
	public NaviMapBean hotel_map;
	/** 会场地图 */
	@Expose
	public List<NaviMapBean> exhibition_map;

}
