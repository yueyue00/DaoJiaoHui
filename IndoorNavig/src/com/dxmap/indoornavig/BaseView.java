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
	protected static ViewGroup m_Main = null; // ������
	protected int m_iLayoutID = -1;

	public static void InitBaseView(ViewGroup main, Context context) {
		m_Main = main;
		m_Context = context;
	}

	/*
	 * �������Ѵ��ڵ�View
	 */
	public boolean onCreateView(View view) {
		m_View = view;
		return true;
	}

	/*
	 * ����layout����View
	 */
	public BaseView onCreateLayout(Class<?> cls, int iLayout) {
		try {
			// ����ָ��Layout��view
			View view = View.inflate(m_Context, iLayout, null);
			// ��ʼ������
			m_iLayoutID = iLayout;
			// ����onCreateView����
			onCreateView(view);
			// ����BaseView����
			return this;
		} catch (Exception e) {
			String strOut = "onCreateLayout error:" + cls.getName();
			e.printStackTrace();
		}
		return null;
	}

	// ����
	public void OnDestroy() {
	}

	//////////////////////////////////////////////////////////////////
	// ����ΪActivity��Ϣ����
	/////////////////////////////////////////////////////////////////
	public boolean onKeyDown(int keyCode, KeyEvent keyevent) {
		// ���¼����Ϸ��ذ�ť
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
	 * �����Ӵ���
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
	 * activity��ʼ���û�����ʱ����
	 * ������������������������һ������÷������Ǳ����õģ���
	 */
	public void onResume() {
	}
	
	
}
