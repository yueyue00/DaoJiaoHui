package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 下榻酒店信息--->嘉宾服务
 * 
 * @author lixiaoming
 * 
 */
public class HotelInformationBean {
	/** 响应码 */
	@Expose
	public int resultCode;
	/** 酒店图片 */
	@Expose
	public String pic;
	/** 酒店名称 */
	@Expose
	public String name;
	/** 酒店位置地点 */
	@Expose
	public String site;
	/** 酒店位置经度 */
	@Expose
	public String longitude;
	/** 酒店位置纬度 */
	@Expose
	public String latitude;
	/** 房间号 */
	@Expose
	public String room;
}
