package com.gj.gaojiaohui.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 点评列表的bean
 * 
 * @author zhangt
 * 
 */
public class DianPingBean implements Serializable {
	@Expose
	public String pic;
	@Expose
	public String name;
	@Expose
	public String date;
	@Expose
	public String icomment_id ;
	@Expose
	public String value;
	@Expose
	public List<PicBean> pic_list;
	@Expose
	public List<ReplyBean> comment_list;
	@Expose
	public Boolean isReplying = false;
}
