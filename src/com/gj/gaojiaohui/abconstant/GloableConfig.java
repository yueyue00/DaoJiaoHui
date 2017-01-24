package com.gj.gaojiaohui.abconstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gj.gaojiaohui.bean.AllUserBean;
import com.gj.gaojiaohui.bean.DiaoDuBean;
import com.gj.gaojiaohui.bean.MenuBean;

/**
 * 定义全局常量的工具类,编写目的:公用常量 占位符的符号为 %s
 */
public class GloableConfig {
	/** 用户名 */
	public static String username = "";
	/** 应用包名 */
	public static String CURRENT_PKGNAME = "com.smartdot.wenbo.huiyi";
	/** 用户id */
	public static String userid = "";
	/** 用户权限 */
	public static String permission = "";
	/** cookie */
	public static String COOKIE = "";
	/** 语言标志位 中文 1，英文为2 */
	public static String LANGUAGE_TYPE = "1";
	/** 中间导航页项目 */
	public static List<MenuBean> menuBeans = new ArrayList<>();
	/** 版本号 */
	public static String VERSION;
	/** 下载最新APK安装包 */
	public static String DOWNLOAD_APK = "";
	/** 所有人员 */
	public static Map<String, AllUserBean> allUserMap = new HashMap<>();
	/** 所有群组 */
	public static Map<String, DiaoDuBean> allGroupMap = new HashMap<>();
	/** 生成个人二维码格式 */
	public static String PERSON_QRCODE = "BEGIN:VCARD\nFN:%s\nEMAIL:%s\nTEL:%s\nID:%s\nORG:%s\nEND:VCARD";
	/** 是否处于离线模式,true为离线,false为正常 */
	public static boolean IS_OFFLINE = false;
	/** 展商日程数据库的路径 */
	public static String DB_PATH = "/data/data/com.smartdot.wenbo.huiyi/files";
	/** cookie的key */
	public static String domain = "app.chtf.com";
	// public static String domain = "10.5.12.70";
	/** 新消息提醒的开关状态 控制极光推送 默认开启极光推送 */
	public static boolean NEW_MESSAGE_STATE = true;

	/************************************* 地址在此行之下 **************************************************/

	/** 根地址 */
//	public static String BASE_URL = "http://wuzhen.smartdot.com:8088/";// 测试服务器
//	public static String BASE_URL = "http://wuzhen.smartdot.com:8088/gaojiao/";// 测试服务器
	public static String BASE_URL = "http://app.chtf.com/";// 正式服务器
	public static String BASE_VIP_URL = "http://eapp.chtf.com/";// VIP高速不掉血服务器

	public static String DEFAULT_BASE_URL = "http://app.chtf.com/";// 默认的正式服务器
	/** 首页接口 */
	public static String HOME_PAGE_URL = BASE_URL + "InfoPublish.do?method=showFirstNews&userid=%s&pageNo=1&language=%s";
	/** 首页-分页新闻列表 */
	public static String HOME_MORE_NEWS_URL = BASE_URL + "InfoPublish.do?method=allNewsList&pageNo=%s&language=%s";
	/** 亮点推荐-列表 */
	public static String LIANGDIAN_URL = BASE_URL + "giftsaction.do?method=GiftAction&language=%s&page=%s";
	/** 亮点推荐-详情 的接口 */
	public static String LIANGDIAN_DETAIL_URL = BASE_URL + "giftsaction.do?method=RecommendedRoute&sedatyId=%s&language=%s";
	/** 通知中心-列表 的接口 */
	public static String NOTIENCE_CENTER_URL = BASE_URL + "hxLastregistercheckaction.do?method=meetingNoticeCenter&userid=%s&page=%s&language=%s";
	/** 大会资讯 的接口 */
	public static String MEETING_NEWS_URL = BASE_URL + "InfoPublish.do?method=meetingList&id=%s&pageNo=%s&language=%s";

	/** 展商列表 的接口 */
	public static String ZHANSHANG_LIST_URL = BASE_URL + "exhibitorsinfo.do?method=getExhibitor&page=%s&language=%s&userId=%s";
	/** 关注展商 的接口 */
	public static String FOLLOW_ZHANSHANG_URL = BASE_URL + "exhibitorsinfo.do?method=addExhibitorFocus&userId=%s&exhibitorId=%s";
	/** 取消关注展商 的接口 */
	public static String CANCLE_FOLLOW_ZHANSHANG_URL = BASE_URL + "exhibitorsinfo.do?method=delFocusExhibitor&userId=%s&audience_id=%s";
	/** 我的关注 的接口 */
	public static String MY_FOLLOW_URL = BASE_URL + "exhibitorsinfo.do?method=getMyFocusExhibitorList&userId=%s&language=%s";
	/** 我的关注搜索 的接口 */
	public static String MY_FOLLOW_SEARCH_URL = BASE_URL + "exhibitorsinfo.do?method=findMyFocusExhibitorList&userId=%s&language=%s&value=%s";
	/** 展商搜索 的接口 */
	public static String ZHANSHANG_SEARCH_URL = BASE_URL + "exhibitorsinfo.do?method=getExhibitor&page=%s&language=%s&userId=%s&value=%s";
	/** 展商详情 的接口 */
	public static String ZHANSHANG_DETAIL_URL = BASE_URL + "exhibitorsinfo.do?method=getExhibitorDetail&userId=%s&exhibitorId=%s&language=%s";

	/** 服务反馈-嘉宾列表 的接口 */
	public static String SERVICE_FEEDBACK_GUEST_LIST_URL = BASE_URL + "subjects.do?method=getServiceFeedBack&userid=%s";
	/** 服务反馈-嘉宾留言列表 的接口 */
	public static String SERVICE_FEEDBACK_GUEST_LEAVE_WORD_URL = BASE_URL + "subjects.do?method=feedBack&userid=%s&vipid=%s";
	/** 留言 的接口 */
	public static String LEAVE_WORD_COMMINT_URL = BASE_URL + "feedBack.do?method=fkliuyan&userid=%s&vipid=%s&value=%s&type=%s";
	/** 留言详情 的接口 */
	public static String LEAVE_WORD_DETAIL_URL = BASE_URL + "exhibitorsinfo.do?method=getMessage&userId=%s&audienceId=%s&isExhibitors=%s";
	/** 展品详情 的接口 */
	public static String EXHIBITOR_DETAIL_URL = BASE_URL + "exhibitorsinfo.do?method=getExhibitorProject&exhibitProjectId=%s&language=%s";
	/** 展商资料 的接口 */
	public static String EXHIBITOR_DATA_URL = BASE_URL + "exhibitorsinfo.do?method=getMyExhibitorDetail&userId=%s&language=%s";
	/** 会议/会展详情 的接口 */
	public static String MEETINT_DETAIL_URL = BASE_URL + "meetings.do?method=huiyiDetail&userid=%s&language=%s&id=%s";
	/** 嘉宾服务 的接口 */
	public static String GUEST_SERVICE_URL = BASE_URL + "activityListAction.do?method=jiaBinFW&userid=%s&language=%s";
	/** 下榻酒店信息 的接口 */
	public static String HOTEL_INFORMATION_URL = BASE_URL + "hotelFloor.do?method=getHotelInfo&userid=%s&language=%s";
	/** 专属服务人员 的接口 */
	public static String EXCLUSIVE_MEMBER_URL = BASE_URL + "vipmembers.do?method=singleFwForMember&userid=%s";

	/** 通讯录 -列表 */
	public static String ADDRESS_BOOK_URL = BASE_URL + "/vipmembers.do?method=getMailList&userid=%s&dept_id=%s";
	/** 通讯录 -搜索 */
	public static String ADDRESS_BOOK_SEARCH_URL = BASE_URL + "/vipmembers.do?method=serachUserMail&userid=%s&value=%s";

	/**
	 * 日程-列表 (占位符位置分别是
	 * 用户id/语言(1代表中文,2代表英文)/日程类型(0代表全部日程,1代表个人日程)/请求的时间(日期如2016-10-14)
	 */
	public static String SCHEDULE_URL = BASE_URL + "hxLastregistercheckaction.do?method=scheduleList" + "&userid=" + "%s" + "&language=" + "%s" + "&type="
			+ "%s" + "&date=" + "%s";

	/** 嘉宾签到-签到按钮 占位符位置分别是 用户id/嘉宾id/签到按钮id */
	public static String VIP_REGISTER = BASE_URL + "taskSign.do?method=honoredGuestSign" + "&userid=" + "%s" + "&vipId=" + "%s" + "&taskid=" + "%s";

	/** 日程-搜索 (占位符位置分别是 用户id/语言(1代表中文,2代表英文)/搜索内容 */
	public static String SCHEDULE_SEARCH_URL = BASE_URL + "hxLastregistercheckaction.do?method=searchForScheduleList" + "&userid=" + "%s" + "&language=" + "%s"
			+ "&title=" + "%s";
	/** 日程-会议/会展 详情 (占位符位置分别是 用户id/语言(1代表中文,2代表英文)/会议/会展详情id) */
	public static String SCHEDULE_DETAIL_URL = BASE_URL + "/meetings.do?method=huiyiDetail" + "&userid=" + "%s" + "&language=" + "%s" + "&id=" + "%s";

	/** 嘉宾签到-列表 (占位符位置是 用户id) */
	public static String VIP_REGISTER_LIST = BASE_URL + "taskSign.do?method=honoredGuestSignList" + "&userid=" + "%s";

	/** 观众互动-搜索 (占位符位置分别是 用户id/搜索内容) */
	public static String AUDIENCE_SEARCH = BASE_URL + "exhibitorsinfo.do?method=findFocusList" + "&userId=" + "%s" + "&value=" + "%s";

	/** 添加日程接口 */
	public static String ADD_SCHEDULE_URL = BASE_URL
			+ "vipmembers.do?method=enterSchedule&userid=%s&language=%s&title=%s&site=%s&date=%s&star=%s&end=%s&remark=%s";
	/** 观众互动-关注我的 (占位符位置分别是 用户id/嘉宾id/签到按钮id) */
	public static String AUDIENCE_FOLLOW_MY = BASE_URL + "exhibitorsinfo.do?method=bMyFocusList" + "&userId=" + "%s";
	/** 观众互动-我关注的 (占位符位置分别是 用户id/嘉宾id/签到按钮id) */
	public static String AUDIENCE_MY_FOLLOW = BASE_URL + "exhibitorsinfo.do?method=myFocusList" + "&userId=" + "%s";
	/** 观众互动-展商关注观众/嘉宾+评星 (占位符位置分别是 用户id/观众id/星级 默认是1) */
	public static String AUDIENCE_FOLLOW = BASE_URL + "exhibitorsinfo.do?method=addPersonFocus" + "&userId=" + "%s" + "&audience_id=" + "%s" + "&star=" + "%s";
	/** 观众互动-取消关注 (占位符位置分别是 用户id/观众id) */
	public static String AUDIENCE_CANCEL_FOLLOW = BASE_URL + "exhibitorsinfo.do?method=delFocusPerson" + "&userId=" + "%s" + "&exhibitorId=" + "%s";
	/** 观众互动-观众详情 (占位符位置分别是 用户id/观众id) */
	public static String AUDIENCE_DETAIL = BASE_URL + "exhibitorsinfo.do?method=detailInfo" + "&userid=" + "%s" + "&audience_id=" + "%s";
	/** 观众互动-观众详情-保存备注 (占位符位置分别是 用户id/观众id/备注内容) */
	public static String AUDIENCE_SAVE_REMARK = BASE_URL + "exhibitorsinfo.do?method=addRemarks" + "&userid=" + "%s" + "&audience_id=" + "%s"
			+ "&remark_value=" + "%s";

	/** 媒体互动-列表 (占位符位置分别是 用户id) */
	public static String MEDIAINTER_ACTION = BASE_URL + "exhibitorsinfo.do?method=bMyAppointmentList" + "&userId=" + "%s";
	/** 媒体互动-媒体详情 (占位符位置分别是 用户id/媒体id) */
	public static String MEDIAINTER_DETAIL = BASE_URL + "exhibitorsinfo.do?method=meidaDetailInfo" + "&userid=" + "%s" + "&audience_id=" + "%s";
	/** 媒体互动-媒体详情-保存备注 (占位符位置分别是 用户id/媒体id/备注内容) */
	public static String MEDIAINTER_SAVE_REMARK = BASE_URL + "exhibitorsinfo.do?method=addRemarksForMedia" + "&userid=" + "%s" + "&audience_id=" + "%s"
			+ "&remark_value=" + "%s";

	/** 大会点评 发布新主题-有图 */
	public static String DIANPING_PUBLIC_URL = BASE_URL + "subjects.do";
	/** 大会点评 发布新主题-无图 */
	public static String DIANPING_PUBLIC_NOPIC_URL = BASE_URL + "subjects.do?method=createTheme2&userid=%s&comment_value=%s";
	/** 大会点评列表接口 */
	public static String DIANPING_LIST_URL = BASE_URL + "subjects.do?method=subjectPage2&userid=%s&page=%s";
	/** 大会点评用户回复接口 */
	public static String DIANPING_REPLE_URL = BASE_URL + "subjects.do?method=replyPingLun&comment_id=%s&reply_id=%s&comment_value=%s";

	/** 嘉宾列表 的接口 */
	public static String GUESTS_LIST_URL = BASE_URL + "vipmembers.do?method=vipList&userid=%s&language=%s&type=%s";
	/** 嘉宾详情 的接口 */
	public static String GUESTS_INFO_URL = BASE_URL + "vipmembers.do?method=vipInfoDetail&userid=%s&language=%s&type=%s&tag=%s";
	/** 嘉宾搜索 的接口 */
	public static String GUESTS_SEARCH_URL = BASE_URL + "vipmembers.do?method=vipList&userid=%s&language=%s&type=%s&value=%s";
	/** 个人资料 的接口 */
	public static String USER_INFO_URL = BASE_URL + "hylogin.do?method=getMemberDetails&userid=%s&lg=%s";
	/** 大会导航 的接口 */
	public static String DAHUINAVI_URL = BASE_URL + "mapNavigation.do?method=mapNavign&userid=%s";

	/** 全部栏目接口 */
	public static String ALL_MENU_URL = BASE_URL + "hylogin.do?method=showScan&userid=%s&language=%s";
	/** 大会车辆接口 */
	public static String CAR_URL = BASE_URL + "InfoPublish.do?method=getBusList&pageNo=%s&language=%s";
	/** 服务调度 */
	public static String DIAODU_URL = BASE_URL + "vipmembers.do?method=getGroupByself&userId=%s";
	/** 各种电话 */
	public static String TEL_URL = BASE_URL + "InfoPublish.do?method=getServiceTel&userId=%s&type=%s&lg=%s";

	/** 保存个人资料 */
	public static String SAVE_PERSON_INFO_URL = BASE_URL + "hylogin.do?method=perfectInfo&userid=%s&name=%s&email=%s&company=%s&position=%s";

	/** 检查更新的接口 */
	public static String CHACK_UPDATA = BASE_URL + "appApk.do?method=appEdition&ostype=1";

	/** 我的报名 的接口 */
	public static String MY_REGISTRATION_URL = BASE_URL + "activityListAction.do?method=signUpList&userId=%s&language=%s";
	/** 活动列表 的接口 */
	public static String HUODONG_LIST_URL = BASE_URL + "activityListAction.do?method=activityListAppInfo&page=%s&language=%s";
	/** 活动详情 的接口 */
	public static String HUODONG_PARTICULARS = BASE_URL + "activityListAction.do?method=activityDetails&userId=%s&actionId=%s&language=%s";
	/** 活动详情 报名 的接口 */
	public static String HUODONG_PARTICULARS_BAOMING = BASE_URL + "activityListAction.do?method=addSignUp&userId=%s&actionId=%s";
	/** 活动详情 取消报名 的接口 */
	public static String HUODONG_PARTICULARS_PASSBAOMING = BASE_URL + "activityListAction.do?method=delSignUp&userId=%s&actionId=%s";

	/** 获取全部人员列表 */
	public static String GET_USER_LIST_URL = BASE_URL + "vipmembers.do?method=getUserCode&userid=%s";
	/** 我的登录(已登录) */
	public static String MY_LOGIN_SUCCESS = BASE_URL + "vipmembers.do?method=getQRcodes&userid=%s&lg=%s";

	/** 大会资料 的接口 type : 1/媒体 2/大会 */
	public static String MEETING_DATAS_URL = BASE_URL + "/fileUpload.do?method=fileDownLoad&type=%s&language=%s";
	/** 采访预约 列表 的接口 */
	public static String INTERVIEW_APPOINTMENT_URL = BASE_URL + "/exhibitorsinfo.do?method=getExhibitorForMedia&page=%s&language=%s";
	/** 采访预约 搜索 的接口 */
	public static String INTERVIEW_APPOINTMENT_SEARCH_URL = BASE_URL + "/exhibitorsinfo.do?method=getExhibitorForMedia&page=%s&language=%s&value=%s";
	/** 预约详情 的接口 */
	public static String APPOINTMENT_URL = BASE_URL + "exhibitorsinfo.do?method=getExhibitorDetailForMedia&userId=%s&exhibitorId=%s&language=%s";
	/** 预约详情-预约 的接口 */
	public static String APPOINTMENT_DETAIL_APPOINTMENT_URL = BASE_URL + "exhibitorsinfo.do?method=addExhibitorAppointment&userId=%s&exhibitorId=%s";
	/** 我的足迹 的接口 */
	public static String MY_TRACKS_URL = BASE_URL + "exhibitorsinfo.do?method=getMyFootprintList&userId=%s&language=%s";
	/** 提交我的足迹 的接口 */
	public static String COMMINT_MY_TRACKS_URL = BASE_URL + "exhibitorsinfo.do?method=addExhibitorFootprint&userId=%s&boothNumber=%s&exhibitorname=%s";
	/** 根据展位号获取展商id和展位号 */
	public static String GET_EXHIBITID_BY_QRCODE_URL = BASE_URL + "exhibitorsinfo.do?method=IndoorNavigation&userId=%s&boothNumber=%s&exhibitorname=%s";
	/** 采访预约-留言 的接口 */
	public static String LEAVEMESSAGE_URL = BASE_URL + "exhibitorsinfo.do?method=getMessageFormedia&userId=%s&audienceId=%s&isExhibitors=%s";
	/** 采访预约发送留言 的接口 */
	public static String COMMIT_LEAVEMESSAGE_URL = BASE_URL + "feedBack.do?method=saveMessage&userid=%s&vipid=%s&value=%s&type=%s";

	/** 提交我的赠票的接口地址 */
	public static String COMMIT_MYTICKET_URL = BASE_URL
			+ "electronicTicketBinding.do?method=electronicTicketBinding&userid=%s&name=%s&cert6=%s&tickid=%s&certtype=%s";
	/** 获取短信验证码请求 */
	public static String GET_VERIFICATION_CODE_URL = BASE_URL + "/hylogin.do?method=yzCode&tel=%s&lg=%s";
	/** 获取盐值 */
	public static String GET_SALT_URL = BASE_URL + "/saltaction.do?method=getSalt&userid=%s&password=%s";
	/** 找回密码 */
	public static String GET_PASSWORD_URL = BASE_URL + "/hylogin.do?method=retrievePassword&tel=%s&password=%s&new_password=%s&user_id=%s";
	/** 手机号注册 的接口 */
	public static String PHONE_REGISTER_URL = BASE_URL + "hylogin.do?method=phoneRegister&tel=%s&number=%s&password=%suser_id=%s";
	/** 手机短信验证码登录 的接口 */
	public static String SMS_VERIFICATION_CODE_LOGIN_URL = "hylogin.do?method=phoneLogin&tel=%s&password=%s&userid=%s";
	/** 账号密码登录 的接口 */
	public static String ACCOUNT_PASSWORD_LOGIN_URL = "hylogin.do?method=passwordLogin&userName=%s&password=%s&lg=%s";

}
