package com.gj.gaojiaohui.utils;

import java.util.Comparator;

import com.gj.gaojiaohui.bean.GuestInfoBean;

public class PinyinGuestListComparator implements Comparator<GuestInfoBean> {

	public int compare(GuestInfoBean bean1, GuestInfoBean bean2) {

		String tmp1 = FirstLetterUtil.getFirstLetter(bean1.name);
		String tmp2 = FirstLetterUtil.getFirstLetter(bean2.name);

		if (tmp1.equals("@") || tmp2.equals("#")) {
			return -1;
		} else if (tmp1.equals("#") || tmp2.equals("@")) {
			return 1;
		} else {
			return tmp1.compareTo(tmp2);
		}
	}

}
