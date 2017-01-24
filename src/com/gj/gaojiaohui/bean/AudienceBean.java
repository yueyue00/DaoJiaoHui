package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 观众详情  - 实体类
 */
public class AudienceBean {
	@Expose
	public int resultCode;
	/**用户姓名 */
	@Expose
	public String name;
	/** 用户星级 1/一般 2/十分 3/非常 */
	@Expose
	public int star;
	/** 关注状态 */
	@Expose
	public boolean follow;
	/** 观众id*/
	@Expose
	public String id;
	/** 所属公司 */
	@Expose
	public String company;
	/** 联系方式 */
	@Expose
	public String tel;
	/** 我的备注内容 */
	@Expose
	public String remark;
	/** 留言列表 只显示前3条 */
	@Expose
	public List<AudienceMsgBean> list;

}
