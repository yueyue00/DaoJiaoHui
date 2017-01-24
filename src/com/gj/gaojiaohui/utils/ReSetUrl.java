package com.gj.gaojiaohui.utils;

import com.gj.gaojiaohui.abconstant.GloableConfig;

/**
 * 对url重新赋值，给vip使用专用服务器
 * 
 * @author zhangt
 * 
 */
public class ReSetUrl {

	/**
	 * 对url重新赋值
	 * 
	 * @param isRest
	 *            true 复位 ；false 切成嘉宾 服务器
	 */
	public static void resetUrl(Boolean isRest) {
		if (!isRest) {
			GloableConfig.BASE_URL = GloableConfig.BASE_VIP_URL;
		} else {
			GloableConfig.BASE_URL = GloableConfig.DEFAULT_BASE_URL;
		}

		/** 首页接口 */
		GloableConfig.HOME_PAGE_URL = GloableConfig.BASE_URL + "InfoPublish.do?method=showFirstNews&userid=%s&pageNo=1&language=%s";
		/** 首页-分页新闻列表 */
		GloableConfig.HOME_MORE_NEWS_URL = GloableConfig.BASE_URL + "InfoPublish.do?method=allNewsList&pageNo=%s&language=%s";
		/** 亮点推荐-列表 */
		GloableConfig.LIANGDIAN_URL = GloableConfig.BASE_URL + "giftsaction.do?method=GiftAction&language=%s&page=%s";
		/** 亮点推荐-详情 的接口 */
		GloableConfig.LIANGDIAN_DETAIL_URL = GloableConfig.BASE_URL + "giftsaction.do?method=RecommendedRoute&sedatyId=%s&language=%s";
		/** 通知中心-列表 的接口 */
		GloableConfig.NOTIENCE_CENTER_URL = GloableConfig.BASE_URL + "hxLastregistercheckaction.do?method=meetingNoticeCenter&userid=%s&page=%s&language=%s";
		/** 大会资讯 的接口 */
		GloableConfig.MEETING_NEWS_URL = GloableConfig.BASE_URL + "InfoPublish.do?method=meetingList&id=%s&pageNo=%s&language=%s";

		/** 展商列表 的接口 */
		GloableConfig.ZHANSHANG_LIST_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=getExhibitor&page=%s&language=%s&userId=%s";
		/** 关注展商 的接口 */
		GloableConfig.FOLLOW_ZHANSHANG_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=addExhibitorFocus&userId=%s&exhibitorId=%s";
		/** 取消关注展商 的接口 */
		GloableConfig.CANCLE_FOLLOW_ZHANSHANG_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=delFocusExhibitor&userId=%s&audience_id=%s";
		/** 我的关注 的接口 */
		GloableConfig.MY_FOLLOW_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=getMyFocusExhibitorList&userId=%s&language=%s";
		/** 我的关注搜索 的接口 */
		GloableConfig.MY_FOLLOW_SEARCH_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=findMyFocusExhibitorList&userId=%s&language=%s&value=%s";
		/** 展商搜索 的接口 */
		GloableConfig.ZHANSHANG_SEARCH_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=getExhibitor&page=%s&language=%s&userId=%s&value=%s";
		/** 展商详情 的接口 */
		GloableConfig.ZHANSHANG_DETAIL_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=getExhibitorDetail&userId=%s&exhibitorId=%s&language=%s";

		/** 服务反馈-嘉宾列表 的接口 */
		GloableConfig.SERVICE_FEEDBACK_GUEST_LIST_URL = GloableConfig.BASE_URL + "subjects.do?method=getServiceFeedBack&userid=%s";
		/** 服务反馈-嘉宾留言列表 的接口 */
		GloableConfig.SERVICE_FEEDBACK_GUEST_LEAVE_WORD_URL = GloableConfig.BASE_URL + "subjects.do?method=feedBack&userid=%s&vipid=%s";
		/** 留言 的接口 */
		GloableConfig.LEAVE_WORD_COMMINT_URL = GloableConfig.BASE_URL + "feedBack.do?method=fkliuyan&userid=%s&vipid=%s&value=%s&type=%s";
		/** 留言详情 的接口 */
		GloableConfig.LEAVE_WORD_DETAIL_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=getMessage&userId=%s&audienceId=%s&isExhibitors=%s";
		/** 展品详情 的接口 */
		GloableConfig.EXHIBITOR_DETAIL_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=getExhibitorProject&exhibitProjectId=%s&language=%s";
		/** 展商资料 的接口 */
		GloableConfig.EXHIBITOR_DATA_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=getMyExhibitorDetail&userId=%s&language=%s";
		/** 会议/会展详情 的接口 */
		GloableConfig.MEETINT_DETAIL_URL = GloableConfig.BASE_URL + "meetings.do?method=huiyiDetail&userid=%s&language=%s&id=%s";
		/** 嘉宾服务 的接口 */
		GloableConfig.GUEST_SERVICE_URL = GloableConfig.BASE_URL + "activityListAction.do?method=jiaBinFW&userid=%s&language=%s";
		/** 下榻酒店信息 的接口 */
		GloableConfig.HOTEL_INFORMATION_URL = GloableConfig.BASE_URL + "hotelFloor.do?method=getHotelInfo&userid=%s&language=%s";
		/** 专属服务人员 的接口 */
		GloableConfig.EXCLUSIVE_MEMBER_URL = GloableConfig.BASE_URL + "vipmembers.do?method=singleFwForMember&userid=%s";

		/** 通讯录 -列表 */
		GloableConfig.ADDRESS_BOOK_URL = GloableConfig.BASE_URL + "/vipmembers.do?method=getMailList&userid=%s&dept_id=%s";
		/** 通讯录 -搜索 */
		GloableConfig.ADDRESS_BOOK_SEARCH_URL = GloableConfig.BASE_URL + "/vipmembers.do?method=serachUserMail&userid=%s&value=%s";

		/**
		 * 日程-列表 (占位符位置分别是
		 * 用户id/语言(1代表中文,2代表英文)/日程类型(0代表全部日程,1代表个人日程)/请求的时间(日期如2016-10-14)
		 */
		GloableConfig.SCHEDULE_URL = GloableConfig.BASE_URL + "hxLastregistercheckaction.do?method=scheduleList" + "&userid=" + "%s" + "&language=" + "%s"
				+ "&type=" + "%s" + "&date=" + "%s";

		/** 嘉宾签到-签到按钮 占位符位置分别是 用户id/嘉宾id/签到按钮id */
		GloableConfig.VIP_REGISTER = GloableConfig.BASE_URL + "taskSign.do?method=honoredGuestSign" + "&userid=" + "%s" + "&vipId=" + "%s" + "&taskid=" + "%s";

		/** 日程-搜索 (占位符位置分别是 用户id/语言(1代表中文,2代表英文)/搜索内容 */
		GloableConfig.SCHEDULE_SEARCH_URL = GloableConfig.BASE_URL + "hxLastregistercheckaction.do?method=searchForScheduleList" + "&userid=" + "%s"
				+ "&language=" + "%s" + "&title=" + "%s";
		/** 日程-会议/会展 详情 (占位符位置分别是 用户id/语言(1代表中文,2代表英文)/会议/会展详情id) */
		GloableConfig.SCHEDULE_DETAIL_URL = GloableConfig.BASE_URL + "/meetings.do?method=huiyiDetail" + "&userid=" + "%s" + "&language=" + "%s" + "&id="
				+ "%s";

		/** 嘉宾签到-列表 (占位符位置是 用户id) */
		GloableConfig.VIP_REGISTER_LIST = GloableConfig.BASE_URL + "taskSign.do?method=honoredGuestSignList" + "&userid=" + "%s";

		/** 观众互动-搜索 (占位符位置分别是 用户id/搜索内容) */
		GloableConfig.AUDIENCE_SEARCH = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=findFocusList" + "&userId=" + "%s" + "&value=" + "%s";

		/** 添加日程接口 */
		GloableConfig.ADD_SCHEDULE_URL = GloableConfig.BASE_URL
				+ "vipmembers.do?method=enterSchedule&userid=%s&language=%s&title=%s&site=%s&date=%s&star=%s&end=%s&remark=%s";
		/** 观众互动-关注我的 (占位符位置分别是 用户id/嘉宾id/签到按钮id) */
		GloableConfig.AUDIENCE_FOLLOW_MY = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=bMyFocusList" + "&userId=" + "%s";
		/** 观众互动-我关注的 (占位符位置分别是 用户id/嘉宾id/签到按钮id) */
		GloableConfig.AUDIENCE_MY_FOLLOW = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=myFocusList" + "&userId=" + "%s";
		/** 观众互动-展商关注观众/嘉宾+评星 (占位符位置分别是 用户id/观众id/星级 默认是1) */
		GloableConfig.AUDIENCE_FOLLOW = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=addPersonFocus" + "&userId=" + "%s" + "&audience_id=" + "%s"
				+ "&star=" + "%s";
		/** 观众互动-取消关注 (占位符位置分别是 用户id/观众id) */
		GloableConfig.AUDIENCE_CANCEL_FOLLOW = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=delFocusPerson" + "&userId=" + "%s" + "&exhibitorId=" + "%s";
		/** 观众互动-观众详情 (占位符位置分别是 用户id/观众id) */
		GloableConfig.AUDIENCE_DETAIL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=detailInfo" + "&userid=" + "%s" + "&audience_id=" + "%s";
		/** 观众互动-观众详情-保存备注 (占位符位置分别是 用户id/观众id/备注内容) */
		GloableConfig.AUDIENCE_SAVE_REMARK = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=addRemarks" + "&userid=" + "%s" + "&audience_id=" + "%s"
				+ "&remark_value=" + "%s";

		/** 媒体互动-列表 (占位符位置分别是 用户id) */
		GloableConfig.MEDIAINTER_ACTION = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=bMyAppointmentList" + "&userId=" + "%s";
		/** 媒体互动-媒体详情 (占位符位置分别是 用户id/媒体id) */
		GloableConfig.MEDIAINTER_DETAIL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=meidaDetailInfo" + "&userid=" + "%s" + "&audience_id=" + "%s";
		/** 媒体互动-媒体详情-保存备注 (占位符位置分别是 用户id/媒体id/备注内容) */
		GloableConfig.MEDIAINTER_SAVE_REMARK = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=addRemarksForMedia" + "&userid=" + "%s" + "&audience_id="
				+ "%s" + "&remark_value=" + "%s";

		/** 大会点评 发布新主题-有图 */
		GloableConfig.DIANPING_PUBLIC_URL = GloableConfig.BASE_URL + "subjects.do";
		/** 大会点评 发布新主题-无图 */
		GloableConfig.DIANPING_PUBLIC_NOPIC_URL = GloableConfig.BASE_URL + "subjects.do?method=createTheme2&userid=%s&comment_value=%s";
		/** 大会点评列表接口 */
		GloableConfig.DIANPING_LIST_URL = GloableConfig.BASE_URL + "subjects.do?method=subjectPage2&userid=%s&page=%s";
		/** 大会点评用户回复接口 */
		GloableConfig.DIANPING_REPLE_URL = GloableConfig.BASE_URL + "subjects.do?method=replyPingLun&comment_id=%s&reply_id=%s&comment_value=%s";

		/** 嘉宾列表 的接口 */
		GloableConfig.GUESTS_LIST_URL = GloableConfig.BASE_URL + "vipmembers.do?method=vipList&userid=%s&language=%s&type=%s";
		/** 嘉宾详情 的接口 */
		GloableConfig.GUESTS_INFO_URL = GloableConfig.BASE_URL + "vipmembers.do?method=vipInfoDetail&userid=%s&language=%s&type=%s&tag=%s";
		/** 嘉宾搜索 的接口 */
		GloableConfig.GUESTS_SEARCH_URL = GloableConfig.BASE_URL + "vipmembers.do?method=vipList&userid=%s&language=%s&type=%s&value=%s";
		/** 个人资料 的接口 */
		GloableConfig.USER_INFO_URL = GloableConfig.BASE_URL + "hylogin.do?method=getMemberDetails&userid=%s&lg=%s";
		/** 大会导航 的接口 */
		GloableConfig.DAHUINAVI_URL = GloableConfig.BASE_URL + "mapNavigation.do?method=mapNavign&userid=%s";

		/** 全部栏目接口 */
		GloableConfig.ALL_MENU_URL = GloableConfig.BASE_URL + "hylogin.do?method=showScan&userid=%s&language=%s";
		/** 大会车辆接口 */
		GloableConfig.CAR_URL = GloableConfig.BASE_URL + "InfoPublish.do?method=getBusList&pageNo=%s&language=%s";
		/** 服务调度 */
		GloableConfig.DIAODU_URL = GloableConfig.BASE_URL + "vipmembers.do?method=getGroupByself&userId=%s";
		/** 各种电话 */
		GloableConfig.TEL_URL = GloableConfig.BASE_URL + "InfoPublish.do?method=getServiceTel&userId=%s&type=%s&lg=%s";

		/** 保存个人资料 */
		GloableConfig.SAVE_PERSON_INFO_URL = GloableConfig.BASE_URL + "hylogin.do?method=perfectInfo&userid=%s&name=%s&email=%s&company=%s&position=%s";

		/** 检查更新的接口 */
		GloableConfig.CHACK_UPDATA = GloableConfig.BASE_URL + "appApk.do?method=appEdition&ostype=1";

		/** 我的报名 的接口 */
		GloableConfig.MY_REGISTRATION_URL = GloableConfig.BASE_URL + "activityListAction.do?method=signUpList&userId=%s&language=%s";
		/** 活动列表 的接口 */
		GloableConfig.HUODONG_LIST_URL = GloableConfig.BASE_URL + "activityListAction.do?method=activityListAppInfo&page=%s&language=%s";
		/** 活动详情 的接口 */
		GloableConfig.HUODONG_PARTICULARS = GloableConfig.BASE_URL + "activityListAction.do?method=activityDetails&userId=%s&actionId=%s&language=%s";
		/** 活动详情 报名 的接口 */
		GloableConfig.HUODONG_PARTICULARS_BAOMING = GloableConfig.BASE_URL + "activityListAction.do?method=addSignUp&userId=%s&actionId=%s";
		/** 活动详情 取消报名 的接口 */
		GloableConfig.HUODONG_PARTICULARS_PASSBAOMING = GloableConfig.BASE_URL + "activityListAction.do?method=delSignUp&userId=%s&actionId=%s";

		/** 获取全部人员列表 */
		GloableConfig.GET_USER_LIST_URL = GloableConfig.BASE_URL + "vipmembers.do?method=getUserCode&userid=%s";
		/** 我的登录(已登录) */
		GloableConfig.MY_LOGIN_SUCCESS = GloableConfig.BASE_URL + "vipmembers.do?method=getQRcodes&userid=%s&lg=%s";

		/** 大会资料 的接口 type : 1/媒体 2/大会 */
		GloableConfig.MEETING_DATAS_URL = GloableConfig.BASE_URL + "/fileUpload.do?method=fileDownLoad&type=%s&language=%s";
		/** 采访预约 列表 的接口 */
		GloableConfig.INTERVIEW_APPOINTMENT_URL = GloableConfig.BASE_URL + "/exhibitorsinfo.do?method=getExhibitorForMedia&page=%s&language=%s";
		/** 采访预约 搜索 的接口 */
		GloableConfig.INTERVIEW_APPOINTMENT_SEARCH_URL = GloableConfig.BASE_URL + "/exhibitorsinfo.do?method=getExhibitorForMedia&page=%s&language=%s&value=%s";
		/** 预约详情 的接口 */
		GloableConfig.APPOINTMENT_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=getExhibitorDetailForMedia&userId=%s&exhibitorId=%s&language=%s";
		/** 预约详情-预约 的接口 */
		GloableConfig.APPOINTMENT_DETAIL_APPOINTMENT_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=addExhibitorAppointment&userId=%s&exhibitorId=%s";
		/** 我的足迹 的接口 */
		GloableConfig.MY_TRACKS_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=getMyFootprintList&userId=%s&language=%s";
		/** 提交我的足迹 的接口 */
		GloableConfig.COMMINT_MY_TRACKS_URL = GloableConfig.BASE_URL
				+ "exhibitorsinfo.do?method=addExhibitorFootprint&userId=%s&boothNumber=%s&exhibitorname=%s";
		/** 根据展位号获取展商id和展位号 */
		GloableConfig.GET_EXHIBITID_BY_QRCODE_URL = GloableConfig.BASE_URL
				+ "exhibitorsinfo.do?method=IndoorNavigation&userId=%s&boothNumber=%s&exhibitorname=%s";
		/** 采访预约-留言 的接口 */
		GloableConfig.LEAVEMESSAGE_URL = GloableConfig.BASE_URL + "exhibitorsinfo.do?method=getMessageFormedia&userId=%s&audienceId=%s&isExhibitors=%s";
		/** 采访预约发送留言 的接口 */
		GloableConfig.COMMIT_LEAVEMESSAGE_URL = GloableConfig.BASE_URL + "feedBack.do?method=saveMessage&userid=%s&vipid=%s&value=%s&type=%s";

		/** 提交我的赠票的接口地址 */
		GloableConfig.COMMIT_MYTICKET_URL = GloableConfig.BASE_URL
				+ "electronicTicketBinding.do?method=electronicTicketBinding&userid=%s&name=%s&cert6=%s&tickid=%s&certtype=%s";
	}
}
