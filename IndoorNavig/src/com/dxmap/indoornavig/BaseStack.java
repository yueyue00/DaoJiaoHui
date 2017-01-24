package com.dxmap.indoornavig;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

public class BaseStack {
	protected static Context m_Context = null;
	protected static ViewGroup m_Main = null; // 主窗口
	protected static View m_ViewTemplate = null;
	// 界面栈列表
	public static ArrayList<BaseView> m_arrList = null;

	/**
	 * 初始化界面栈
	 * 
	 * @param main
	 * @param context
	 */
	public static void InitBaseStack(ViewGroup main, Context context) {
		m_Main = main;
		m_Context = context;
		m_arrList = new ArrayList<BaseView>();
	}

	/**
	 * 释放界面栈
	 */
	public static void UnInitBaseStack() {
		if (m_arrList == null)
			return;
		int i = 0;
		int iSize = m_arrList.size();
		for (; i < iSize - 1; ++i) {
			m_arrList.get(i).OnDestroy();
		}
		RemoveAllView();
		// m_Main.setContentView(null);
		m_arrList = null;
	}

	public static void SetContentView(BaseView baseview) {
		if (baseview == null || baseview.m_View == null)
			return;
		m_Main.addView(baseview.m_View);
	//	((Activity)m_Context).overridePendingTransition(R.anim.textnavi_open, 0);
	}

	/**
	 * 移除全部界面
	 */
	public static void RemoveAllView() {
		// 此处不更新view
		int iSize = m_arrList.size();
		if (iSize != 0) {
			int i = iSize - 1;
			for (; i >= 0; --i) {
				m_arrList.get(i).OnDestroy();
			}
			m_arrList.clear();
		}
	}

	/**
	 * 创建View并添加到界面栈
	 * 
	 * @param cls
	 * @param iLayout
	 * @return 创建成功返回BaseView，否则返回null
	 */
	public static BaseView CreateViewAndAddView(Class<?> cls, int iLayout) {
		BaseView baseview = null;
		try {
			// 实例化BaseView对象
			baseview = (BaseView) cls.newInstance();
			// 调用onCreateView方法
			baseview.onCreateLayout(cls, iLayout);
		} catch (Exception e) {
			String strOut = "onCreateLayout error:" + cls.getName();
			e.printStackTrace();
		}

		if (baseview != null && baseview.m_View != null) {
			m_arrList.add(baseview);
			// m_Main.setContentView( baseview.m_View );
			UpdateViewStatus();
			return baseview;
		} else {
			String strOut = "onCreateLayout error:" + cls.getName();
		}
		return null;
	}

	/**
	 * 刷新界面View状态
	 */
	private static void UpdateViewStatus() {
		int i = 0;
		int iSize = m_arrList.size();
		for (; i < iSize - 1; ++i) {
			if (m_arrList.get(i).m_View.getVisibility() == View.VISIBLE)
				m_arrList.get(i).m_View.setVisibility(View.INVISIBLE);
		}

		if (m_arrList.get(iSize - 1).m_View.getVisibility() != View.VISIBLE)
			m_arrList.get(iSize - 1).m_View.setVisibility(View.VISIBLE);
		// m_arrList.get( iSize - 1 ).onFocusChanged( true );
	}

	public static int GetStackSize() {
		int size = m_arrList != null ? m_arrList.size() : 0;
		return size;
	}

	/**
	 * 移除栈顶界面
	 * 
	 * @return 返回栈顶BaseView
	 */
	public static BaseView RemoveLastView() {
		// int iSize = m_arrList!=null?m_arrList.size( ):0;
		int iSize = m_arrList.size();
		if (iSize == 0)
			return null;

		m_arrList.get(iSize - 1).OnDestroy();
		m_arrList.remove(iSize - 1);
		m_Main.removeViewAt(iSize - 1);
		if (iSize == 1)
			return null;

		// m_Main.setContentView( m_arrList.get( iSize - 2 ).m_View );
		UpdateViewStatus();
		return m_arrList.get(iSize - 2);
	}

	public static BaseView GetTopStack() {

		int iSize = m_arrList != null ? m_arrList.size() : 0;
		if (iSize == 0)
			return null;
		return m_arrList.get(iSize - 1);
	}
	
	/**
	 * 得到指定界面
	 * @param i
	 * @return
	 */
	public static BaseView GetStack( int i )
	{
		int iSize = m_arrList.size( );
		if ( i < 0 || i >= iSize)
			return null;
		return m_arrList.get( i );
	}
}
