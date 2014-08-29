package com.tianci.pppoe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.tianci.pppoe.R;
import com.tianci.pppoe.utils.LogUtil;


public class BgScrollView extends ScrollView
{
	private ImageView image = null;
	private Scroller mScroller;
	private float div  = 1 ; 
	
	private ScrollStatusListener mListener = null;
	public interface ScrollStatusListener
	{
		public void onScrollFinished(boolean isFinished);
	}
	
	public BgScrollView(Context context,float div)
	{
		super(context);
		this.div = div;
		setFocusable(false);
		setFocusableInTouchMode(false);
		setClickable(false);
		setLayoutParams(new LayoutParams(-1, -1));
		setVerticalScrollBarEnabled(false);
		
		image = new ImageView(context);
		image.setImageResource(R.drawable.bg);
		image.setScaleType(ScaleType.FIT_CENTER);
		mScroller = new Scroller(getContext(),new AccelerateInterpolator());
		addView(image,new LayoutParams(-1, -1));
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		// TODO Auto-generated method stub
//		return super.onTouchEvent(ev);
		return true;
	}
	
	
	@Override
	public void computeScroll()
	{

		if (mScroller.computeScrollOffset())
		{		
			LogUtil.d("BgScrollView", "computeScroll");
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			LogUtil.d("BgScrollView", "s.x = "+mScroller.getCurrX()+"  s.y = "+mScroller.getCurrY());
			LogUtil.d("BgScrollView", "Scrollx = "+getScrollX()+"  Scrolly = "+getScrollY());
			postInvalidate();
		}else {
			if (mListener != null)
			{
				mListener.onScrollFinished(true);
			}
		}
	}

	public Scroller getScroller()
	{
		return mScroller;
	}

	public void setScroller(Scroller scroller)
	{
		this.mScroller = scroller;
	}
	public ScrollStatusListener getScrollStatusListener()
	{
		return mListener;
	}
	public void setScrollStatusListener(ScrollStatusListener mListener)
	{
		this.mListener = mListener;
	}
	
}
