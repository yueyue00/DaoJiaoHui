package com.gj.gaojiaohui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
/**
 * 自定ListView 为了解决 ScrollowView嵌套Lis他View，Listview内容太多显示不全
 * @author lixiaoming
 *
 */
public class CustomListview extends ListView {
	public CustomListview(Context context) {
		super(context);
	}

	public CustomListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
