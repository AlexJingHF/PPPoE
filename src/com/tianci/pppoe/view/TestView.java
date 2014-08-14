package com.tianci.pppoe.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.SweepGradient;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;

public class TestView extends View
{

	int[] colors = new int[]{0XFF9BCA41,0XFF75C761,0XFF1DBEAE,0XFF11B5C8,0XFF2CABC8,0XFF5F9EC9,0XFF9790C8
			,0XFFE585BD,0XFFFB8B9E,0XFFF99778,0XFFFAA94E,0XFFF8B736,0XFFE6BF36,0XFFC7C735,0XFFA4CF3F,0XFF9BCA41};
	private Paint mPaint;
	private TextSwitcher ts ;
	public TestView(Context context)
	{
			super(context);
//			mPaint = new Paint();
//			SweepGradient l = new SweepGradient(200, 200, colors, null);
//			mPaint.setShader(l);
//			mPaint.setStyle(Style.STROKE);
//			mPaint.setStrokeWidth(20);
			setTs(new TextSwitcher(getContext()));
			TextView t1 = new TextView(getContext());
			t1.setText("eth01");
			TextView t2 = new TextView(getContext());
			getTs().addView(t1);
			getTs().addView(t2);
			
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawCircle(200, 200, 200, mPaint);
	}

	public TextSwitcher getTs()
	{
		return ts;
	}

	public void setTs(TextSwitcher ts)
	{
		this.ts = ts;
	}
	

}
