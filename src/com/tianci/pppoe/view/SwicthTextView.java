package com.tianci.pppoe.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.tianci.pppoe.R;

public class SwicthTextView extends RelativeLayout implements OnClickListener,
		ViewFactory
{
	private static final String TAG = SwicthTextView.class.getSimpleName();

	private float div = 1;

	private TextSwitcher mTextSwitcher = null;
	private ImageView mLeftIcon = null;
	private ImageView mRightIcon = null;

	private int mWidth = 390;
	private int mHeight = 105;
	private int mArrowSize = 45;
	private int mMargin = 24;
	private int mSwitchWidth = 252;
	private int mIndex = 0;
	private String[] strings = null;
	private static final float TEXT_SIZE = 35;

	public SwicthTextView(Context context, float div)
	{
		super(context);
		this.div = div;
		ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
				(int) (mWidth / div), (int) (mHeight / div));
		setLayoutParams(vlp);
		setBackgroundResource(R.drawable.switch_box_selector);
		setFocusable(true);
		setFocusableInTouchMode(true);
		addContent();
		initUI();
		addUI();
	}

	private void addUI()
	{
		RelativeLayout.LayoutParams leftlp = new LayoutParams(
				(int) (mArrowSize / div), (int) (mArrowSize / div));
		leftlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		leftlp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		leftlp.leftMargin = (int) (mMargin / div);
		addView(mLeftIcon, leftlp);

		RelativeLayout.LayoutParams rightlp = new LayoutParams(
				(int) (mArrowSize / div), (int) (mArrowSize / div));
		rightlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		rightlp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		rightlp.rightMargin = (int) (mMargin / div);
		addView(mRightIcon, rightlp);

		RelativeLayout.LayoutParams switchlp = new LayoutParams(
				(int) (mSwitchWidth / div), -2);
		switchlp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		addView(mTextSwitcher, switchlp);
	}

	private void initUI()
	{
		mTextSwitcher = new TextSwitcher(getContext());
		mTextSwitcher.setFactory(this);
		mTextSwitcher.setText(strings[0]);

		mLeftIcon = new ImageView(getContext());
		mLeftIcon.setBackgroundResource(R.drawable.left_arrow_selector);
		mLeftIcon.setOnClickListener(this);

		mRightIcon = new ImageView(getContext());
		mRightIcon.setBackgroundResource(R.drawable.right_arrow_selector);
		mRightIcon.setOnClickListener(this);
	}

	private void addContent()
	{
		strings = getResources().getStringArray(R.array.network_interface);
		mIndex = 0;
	}

	@Override
	public void onClick(View v)
	{
		if (v.equals(mLeftIcon))
		{
			if (mIndex > 0)
			{
				mTextSwitcher.setInAnimation(AnimationUtils.loadAnimation(
						getContext(), R.anim.right_in));
				mTextSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
						getContext(), R.anim.left_out));
				mIndex--;
				mTextSwitcher.setText(strings[mIndex]);
			}
		} else if (v.equals(mRightIcon))
		{
			if (mIndex < strings.length - 1)
			{
				mTextSwitcher.setInAnimation(AnimationUtils.loadAnimation(
						getContext(), R.anim.left_in));
				mTextSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
						getContext(), R.anim.right_out));
				mIndex++;
				mTextSwitcher.setText(strings[mIndex]);
			}
		}
	}

	@Override
	public View makeView()
	{
		TextView tv = new TextView(getContext());
		tv.setTextColor(Color.WHITE);
		tv.setTextSize(TEXT_SIZE);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_LEFT:
			onClick(mLeftIcon);
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			onClick(mRightIcon);
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public String getCurrentString()
	{
		return strings[mIndex];
	}
}
