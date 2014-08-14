package com.tianci.pppoe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tianci.pppoe.R;

public class EditTextWithImg extends EditText
{
	private static final String TAG = EditTextWithImg.class.getSimpleName();
	private float div = 1;
	private float size = 45f;
	private int iconPaddingLeft = 45;
	private int iconPaddingTop = 30;
	private int diffX = 10;
	public static int mWidth = 625;
	public static int mHeight = 105;
	
	private Bitmap mIcon = null;
	private Paint mPaint = null;
	public EditTextWithImg(Context context,float div)
	{
		super(context);
		this.div = div;
		setBackgroundResource(R.drawable.edit_box_selector);
		ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams((int)(mWidth/div),(int)(mHeight/div));
		setLayoutParams(vlp);
		setPadding((int) ((iconPaddingLeft+size+diffX)/div), 0, 0, 0);
		mPaint = new Paint();
	}
	
	public void setIcon(int resId)
	{
		mIcon = BitmapFactory.decodeResource(getResources(), resId);
		if (mIcon != null)
		{
			postInvalidate();
		}
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (mIcon!=null)
		{
			canvas.drawBitmap(mIcon, iconPaddingLeft/div, iconPaddingTop/div, mPaint);
		}
	}
}
