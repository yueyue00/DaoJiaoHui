package com.dxmap.indoornavig;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

public class BaseView {

	protected View m_View = null;
	protected static Context m_Context = null;
	protected static ViewGroup m_Main = null; // 主窗口
	protected int m_iLayoutID = -1;

	public static void InitBaseView(ViewGroup main, Context context) {
		m_Main = main;
		m_Context = context;
	}

	/*
	 * 将创建已存在的View
	 */
	public boolean onCreateView(View view) {
		m_View = view;
		return true;
	}

	/*
	 * 根据layout创建View
	 */
	public BaseView onCreateLayout(Class<?> cls, int iLayout) {
		try {
			// 创建指定Layout的view
			View view = View.inflate(m_Context, iLayout, null);
			// 初始化参数
			m_iLayoutID = iLayout;
			// 调用onCreateView方法
			onCreateView(view);
			// 返回BaseView对象
			return this;
		} catch (Exception e) {
			String strOut = "onCreateLayout error:" + cls.getName();
			e.printStackTrace();
		}
		return null;
	}

	// 销毁
	public void OnDestroy() {
	}

	//////////////////////////////////////////////////////////////////
	// 以下为Activity消息处理
	/////////////////////////////////////////////////////////////////
	public boolean onKeyDown(int keyCode, KeyEvent keyevent) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (BaseStack.GetStackSize() <= 1) {
				// OnCreateDialog( UiBaseDefined.DIALOG_EXIT_MESSAGE );
			} else {
				BaseStack.RemoveLastView();
			//	BaseStack.SetContentView(view);
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 查找子窗口
	 */
	public final View findViewById(int id) {
        if (id < 0 || m_View == null) {
            return null;
        }
       return m_View.findViewById(id);
    }
	
	public boolean onActivityResult( int requestCode, int resultCode, Intent data )
	{
		return false;
	}
	
	/**
	 * activity开始与用户交互时调用
	 * （无论是启动还是重新启动一个活动，该方法总是被调用的）。
	 */
	public void onResume() {
	}
	
	
}
