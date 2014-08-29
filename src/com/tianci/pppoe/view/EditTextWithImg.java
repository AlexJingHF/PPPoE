package com.tianci.pppoe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tianci.pppoe.R;
import com.tianci.pppoe.utils.Config;

public class EditTextWithImg extends EditText
{
	private static final String TAG = EditTextWithImg.class.getSimpleName();
	private float div = Config.getDiv();
	private float size = 45f;
	private int iconPaddingLeft = 45;
	private int iconPaddingTop = 30;
	private int diffX = (int)(10/Config.getDiv());
	public static int mWidth = 625;
	public static int mHeight = 105;
	
	private Drawable mIcon = null;
	public EditTextWithImg(Context context)
	{
		super(context);
		setBackgroundResource(R.drawable.edit_box_selector);
		ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams((int)(mWidth/div),(int)(mHeight/div));
		setLayoutParams(vlp);
		setPadding((int) (iconPaddingLeft/div), 0, (int) (iconPaddingLeft/div), 0);
        setCompoundDrawablePadding(diffX);
	}
	
	public void setIcon(int resId)
	{
		mIcon =getResources().getDrawable(resId);
		if (mIcon != null)
		{
			setCompoundDrawablesWithIntrinsicBounds(mIcon,null,null,null);
		}
	}

}
