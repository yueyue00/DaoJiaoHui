package com.dxmap.indoornavig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fengmap.android.analysis.navi.FMNaviAnalyser;
import com.fengmap.android.analysis.navi.FMNaviModule;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.geometry.FMMapCoord;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class ExecuteNaviTask {
	public interface OnNaviTaskResultListener{
		public void onNaviTaskResult(int type,FMNaviAnalyser analyser);
	}
	private FMMap mMap;
	private FMNaviAnalyser mNaviAnalyser;
	private int mStartGroupId;
	private int mEndGroupId;
	private FMMapCoord mStartPt;
	private FMMapCoord mEndPt;
	private OnNaviTaskResultListener mListener;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				int type = (Integer) msg.obj;
				if(mListener != null)
					mListener.onNaviTaskResult(type, mNaviAnalyser);
				break;

			default:
				break;
			}
		}
		
	};
	public ExecuteNaviTask() {
		// TODO Auto-generated constructor stub
	}
	
	public void setListener(OnNaviTaskResultListener listener){
		mListener = listener;
	}
	
	public void ExecuteNavi(FMMap map,FMNaviAnalyser analyser,int startGroupId, FMMapCoord startPt, int endGroupId, FMMapCoord endPt){
		mMap = map;
		mNaviAnalyser = analyser;
		mStartGroupId = startGroupId;
		mEndGroupId =endGroupId;
		mStartPt = startPt;
		mEndPt = endPt;
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		singleThreadExecutor.execute(runnable);
	}
	
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			/*if (mNaviAnalyser != null){
				mNaviAnalyser.release();
				mNaviAnalyser = null;
			}
				*/
		//	mNaviAnalyser = FMNaviAnalyser.init(mMap);
			int type = mNaviAnalyser.analyzeNavi(mStartGroupId, mStartPt, mEndGroupId, mEndPt,FMNaviModule.MODULE_BEST);
			Message message = new Message();
			message.obj = type;
			message.what = 1;
			mHandler.sendMessage(message);
		}
	};

}
