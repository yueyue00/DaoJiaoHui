package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 检查更新
 * @author Administrator
 *
 */
public class CheckUpdateBean {

	@Expose
	public int resultCode;
	/**当前最新版本号 */
	@Expose
	public String versions;
	/**当前最新安装包 */
	@Expose
	public String down_url;
}
