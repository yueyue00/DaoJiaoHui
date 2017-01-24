package com.dxmap.indoornavig.test;

import com.dxmap.indoornavig.BaseStack;
import com.dxmap.indoornavig.BaseView;
import com.dxmap.indoornavig.IndoorHomeView;
import com.dxmap.indoornavig.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FragmentTwo extends Fragment implements OnKeyListener{
	
	private FrameLayout mMainView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_two, container ,false);
		
		mMainView  = (FrameLayout) v.findViewById(R.id.main_view);
		BaseStack.InitBaseStack(mMainView, this.getActivity());
		BaseView.InitBaseView(mMainView, this.getActivity());
		BaseView home = BaseStack.CreateViewAndAddView(IndoorHomeView.class, R.layout.view_home);
		BaseStack.SetContentView(home);
		
		 // 设置下面两个状态才能监听返回事件  
        v.setFocusable(true);  
        v.setFocusableInTouchMode(true);  
        v.setOnKeyListener(this);  
		return v;
	}
	

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		BaseView baseview = BaseStack.GetTopStack();
		if (baseview != null) {
			if (baseview.onKeyDown(keyCode, event) == true)
				return true;
		}
		if (BaseStack.GetStackSize() <= 1) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				BaseStack.UnInitBaseStack();
				//销毁窗口操作
				
				return true;
			}
		}
		return false;
	}

}
