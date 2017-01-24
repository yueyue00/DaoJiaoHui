package com.dxmap.indoornavig;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class ShrinkView extends ImageView {
	private int lastX;
	private int lastY;
    private int bottomloc ;
    private boolean is = true;
    private OnShrinkOffsetListener mListener;
	public ShrinkView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ShrinkView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public ShrinkView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	public void setScreenHeight(int h){
		bottomloc = h;
	}
	
	public void setListener(OnShrinkOffsetListener listener){
		mListener = listener;
	}

	public boolean onTouchEvent(MotionEvent event) {
	    //获取到手指处的横坐标和纵坐标
	    int x = (int) event.getX();
	    int y = (int) event.getY();
	    switch(event.getAction()){
	      case MotionEvent.ACTION_DOWN:
	        lastX = x;
	        lastY = y;
	      break;
	      case MotionEvent.ACTION_MOVE:
	        //计算移动的距离
	        int offX = x - lastX;
	        int offY = y - lastY;
	       
	        if(mListener != null){
	        	mListener.onShrinkOffset(offY);
	        }
	        //调用layout方法来重新放置它的位置
	       /* layout(getLeft(), getTop()+offY,
	          getRight(), getBottom()+offY);*/
	        
	        //Log.i("layout: ", "left:"+getLeft()+"  top:"+getTop()+offY+"  right："+getRight()+"  bottom:"+getBottom());
	      break;
	      case MotionEvent.ACTION_UP:
	    	  if(mListener != null){
		        	mListener.onShrinkUp(getLeft(), getTop(),
		      	          getRight(), getBottom());
		        }
	    	  break;
	    }
	    return true;
	  }

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}
	
	public interface OnShrinkOffsetListener{
		public boolean onShrinkOffset(int offset);
		public void onShrinkUp(int l,int t,int r,int b);
	}

}
