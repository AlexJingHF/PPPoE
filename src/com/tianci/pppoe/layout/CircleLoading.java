package com.tianci.pppoe.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;

public class CircleLoading extends View
{
	private static final String TAG = CircleLoading.class.getSimpleName();

	int[] colors = new int[] { 0XFF9BCA41, 0XFF75C761, 0XFF1DBEAE, 0XFF11B5C8,
			0XFF2CABC8, 0XFF5F9EC9, 0XFF9790C8, 0XFFE585BD, 0XFFFB8B9E,
			0XFFF99778, 0XFFFAA94E, 0XFFF8B736, 0XFFE6BF36, 0XFFC7C735,
			0XFFA4CF3F, 0XFF9BCA41 };
	private float div = 1f;
	private Rect mRect = null;

	private float mSizeOuter = 504f;
	private float mSizeInner = 360f;
	private float mStrokeSize = 300f;
	private float mRadiusOuter = 252f;
	private float mRadiusInner = 180f;
	private float mRadiusStroke = 150f;
	private float mRadiusTip = 216f;
	private float mStrokeWidth = 12f;
	private static final int COLOR_WHITE = 0XFFFFFFFF;
	private static final int COLOR_WHITE_TRANSLATE = 0X7FFFFFFF;
	private static final int COLOR_GRAY = 0XFF888888;
	private static final int COLOR_TEXT_PROGRESS = 0XFF54CCDC;
	private static final int COLOR_TEXT_LOADING = 0XFF1F81A6;
	private static final int COLOR_BLACK = 0XFF000000;

	private static final int TEXT_SIZE_PROGRESS = 90;
	private static final int TEXT_SIZE_LOADING = 40;
	private static final int TEXT_SIZE_TIP = 40;

	private Paint mPaintCircleFill = null;
	private Paint mPaintCircleStroke = null;
	private Paint mPaintText = null;
	private Paint mPaintProgress = null;

	private float mProgressLeft = 105f;
	private float mProgressRight = 105f;

	private RectF mRectStroke = null;
	private RectF mRectTip = null;

	private float mSweepAngle = 0;

	private float mTipSweepAngleStart = -90;

	private int mProgress = 0;
	private float mTextCenterY = 0;

	private String mLoading = "Loading";
	private String mTip = "PPPoE网络连接中...";
	private int mTipAngle = 0;
	private float mTextPadding = 45f;

	private Path mTextTipPath = null;
	private int mTotleTime = 60000;
	private float mStep = 1;
	private long mInterval = 30;
	public interface CircleLoadingListener
	{
		public void onFinished(boolean isFinish);
		public void onCurrentProgress(int progress);
	}
	
	private CircleLoadingListener mListener = null;
	
	public CircleLoading(Context context, float div)
	{
		super(context);
		this.div = div;
		this.setLayoutParams(new ViewGroup.LayoutParams((int)(mSizeOuter/div),(int)(mSizeOuter/div)));
		mPaintCircleFill = new Paint();
		mPaintCircleFill.setStyle(Style.FILL);
		mPaintCircleFill.setAntiAlias(true);

		mPaintCircleStroke = new Paint();
		mPaintCircleStroke.setStyle(Style.STROKE);
		mPaintCircleStroke.setStrokeWidth(mStrokeWidth / div);
		mPaintCircleStroke.setColor(COLOR_WHITE);
		mPaintCircleStroke.setAntiAlias(true);

		mPaintText = new Paint();
		mPaintText.setAntiAlias(true);
		mPaintText.setStyle(Style.FILL);
		mPaintText.setColor(COLOR_TEXT_PROGRESS);
		mPaintText.setTextAlign(Align.CENTER);
		mPaintText.setTextSize(TEXT_SIZE_PROGRESS / div);
		mPaintText.setTypeface(Typeface.MONOSPACE);
		setTextCenter();

		mPaintProgress = new Paint();
		mPaintProgress.setShader(new SweepGradient(mRadiusOuter / div,
				mRadiusOuter / div, colors, null));
		mPaintProgress.setStrokeWidth(mStrokeWidth / div);
		mPaintProgress.setStyle(Style.STROKE);
		mPaintProgress.setAntiAlias(true);
		mRectStroke = new RectF(mProgressLeft / div, mProgressRight / div,
				(mProgressLeft + mStrokeSize) / div,
				(mProgressLeft + mStrokeSize) / div);
		mTextTipPath = new Path();

		mRectTip = new RectF((mRadiusOuter - mRadiusTip) / div,
				(mRadiusOuter - mRadiusTip) / div, (mRadiusOuter + mRadiusTip)
						/ div, (mRadiusOuter + mRadiusTip) / div);
		mTextTipPath.addArc(mRectTip, mTipSweepAngleStart, 180);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		mPaintCircleFill.setColor(COLOR_WHITE_TRANSLATE);
		canvas.drawCircle(mRadiusOuter / div, mRadiusOuter / div, mRadiusOuter
				/ div, mPaintCircleFill);
		mPaintCircleFill.setColor(COLOR_WHITE);
		canvas.drawCircle(mRadiusOuter / div, mRadiusOuter / div, mRadiusInner
				/ div, mPaintCircleFill);
		canvas.drawArc(mRectStroke, -90f, mSweepAngle, false, mPaintProgress);
		mPaintText.setColor(COLOR_TEXT_PROGRESS);
		mPaintText.setTextSize(TEXT_SIZE_PROGRESS / div);
		canvas.drawText(mProgress + "%", mRadiusOuter / div,
				mTextCenterY / div, mPaintText);
		mPaintText.setColor(COLOR_TEXT_LOADING);
		mPaintText.setTextSize(TEXT_SIZE_LOADING / div);
		canvas.drawText(mLoading, mRadiusOuter / div,
				(mTextCenterY + mTextPadding) / div, mPaintText);
		mPaintText.setColor(COLOR_TEXT_LOADING);
		canvas.drawTextOnPath(getTip(), mTextTipPath, 0, 0, mPaintText);
	}

	private void setTextCenter()
	{
		Rect textRect = new Rect();
		mPaintText.getTextBounds(mProgress + "", 0, 1, textRect);
		float fHeight = textRect.height();
		mTextCenterY = mRadiusOuter + fHeight / 2;
	}

	class TipLoading implements Runnable
	{
		Thread t;
		public TipLoading()
		{
			t = new Thread(this);
			t.start();
		}

		public void stop()
		{
			t.interrupt();
		}
		
		@Override
		public void run()
		{
			while (true && !isStop)
			{
				mTextTipPath.reset();
				mTipSweepAngleStart--;
				if (mTipSweepAngleStart == (-360 + mTipAngle))
				{
					mTipSweepAngleStart = 0;
				}
				mTextTipPath.addArc(mRectTip, mTipSweepAngleStart, 180);
				postInvalidate();
				try
				{
					Thread.sleep(mInterval);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

	}

	class PercentThread implements Runnable
	{
		Thread t;

		public PercentThread()
		{
			t = new Thread(this);
			t.start();
		}

		public void stop()
		{
			t.interrupt();
		}

		@Override
		public void run()
		{
			while (mSweepAngle < 360 && !isStop)
			{
					mSweepAngle +=mStep;
					if (mSweepAngle > 360)
					{
						mSweepAngle = 360;
					}
					mProgress = (int) (mSweepAngle / 360 * 100);
					try
					{
						Thread.sleep(mInterval);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}

			}
			if (mListener!=null)
			{
				mListener.onFinished(true);
			}
		}
	}

	class LoadingThread implements Runnable
	{
		Thread t;

		public LoadingThread()
		{
			t = new Thread(this);
			t.start();
		}

		public void stop()
		{
			t.interrupt();
		}

		@Override
		public void run()
		{
			while (true && !isStop)
			{
				if (mLoading.length() < 10)
				{
					mLoading += ".";
				} else
				{
					mLoading = "Loading";
				}
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}

			}
		}
	}

	PercentThread pt = null;
	TipLoading tl = null;
	LoadingThread lt = null;

	public void start(int time)
	{
		mTotleTime = time;
		mStep = 360f/mTotleTime*mInterval;
		this.start();
	}
	
	public void start()
	{
		pt = new PercentThread();
		tl = new TipLoading();
		lt = new LoadingThread();
	}

	private boolean isStop = false;

	public void stop()
	{
		isStop = true;
//		pt.stop();
//		tl.stop();
//		lt.stop();
		pt = null;
		tl = null;
		lt = null;
		initData();
		isStop = false;
	}
	
	private void initData()
	{
		mProgress = 0;
		mSweepAngle = 0;
	}

	public String getTip()
	{
		return mTip;
	}

	public void setTip(String mTip)
	{
		this.mTip = mTip;
	}

	public CircleLoadingListener getCircleLoadingListener()
	{
		return mListener;
	}

	public void setCircleLoadingListener(CircleLoadingListener mListener)
	{
		this.mListener = mListener;
	}
	
}
