package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 通知中心-子bean类
 * 
 * @author lixiaoming
 * 
 */
public class NoticeCenterChildBean {
	/** 消息时间 */
	@Expose
	public String date;
	/** 内容 */
	@Expose
	public String content;
	/** 类型 0/紧急通知,1/会议通知,2/会展通知,3/报名活动通知,4/嘉宾日程通知,5/展商[和]观众/嘉宾互相留言通知*/
	@Expose
	public String type;
	/** 通知标题 */
	@Expose
	public String title;
}
