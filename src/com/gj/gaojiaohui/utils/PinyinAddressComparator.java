package com.gj.gaojiaohui.utils;

import java.util.Comparator;

import com.gj.gaojiaohui.bean.AddressBean;

public class PinyinAddressComparator implements Comparator<AddressBean> {

	public int compare(AddressBean bean1, AddressBean bean2) {

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
