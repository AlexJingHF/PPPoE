package com.tianci.pppoe.layout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianci.pppoe.R;
import com.tianci.pppoe.activity.MainActivity;
import com.tianci.pppoe.utils.Config;
import com.tianci.pppoe.utils.LogUtil;

public class StatusView extends LinearLayout implements OnClickListener
{
	private static final String TAG = StatusView.class.getSimpleName();

	private int mWidth = 981;
	private float div = Config.getDiv();
	
	private float mTextSize = 35;
	private int mPaddingTop = 40;
	private String[] strings = null;
	private String[] statusStrings = null;
	private List<TextView> statusList = null;
	private Button mClose = null;
	private Button mDisconnect = null;

	public StatusView(Context context, float div)
	{
		super(context);
		this.div = div;
		setOrientation(LinearLayout.VERTICAL);
		setLayoutParams(new ViewGroup.LayoutParams((int)(mWidth/div), -2));
		setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
		initUI();
	}

	private void initUI()
	{
		strings = getResources().getStringArray(R.array.status);
		statusList = new ArrayList<TextView>();
		for (String string : strings)
		{
			LinearLayout ll = new LinearLayout(getContext());
			ll.setOrientation(HORIZONTAL);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(-1,
					-2);
			ll.setPadding(0, (int) (mPaddingTop/div), 0, 0);
			ll.setLayoutParams(llp);
			TextView tv1 = getTextView();
			tv1.setText(string);
			TextView tv2 = getTextView();
			statusList.add(tv2);
			LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams(-2,
					-2);
			ll.addView(tv1, llp1);
			ll.addView(tv2, llp1);

			addView(ll);
		}
		LinearLayout ll = new LinearLayout(getContext());
		ll.setOrientation(HORIZONTAL);
		ll.setPadding(0, (int) (mPaddingTop/div), 0, 0);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(-1, -2);
		llp.weight = 2;
		mClose = getButton();
		mClose.setOnClickListener(this);
		mClose.setBackgroundResource(R.drawable.button_small_selector);
		mClose.setText(R.string.close);
		LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams(-2, -2);
		llp1.weight = 1;
		mDisconnect = getButton();
		mDisconnect.setOnClickListener(this);
		mDisconnect.setText(R.string.disconnect);
		mDisconnect.setBackgroundResource(R.drawable.button_small_selector);
		ll.addView(mClose, llp1);
		ll.addView(mDisconnect, llp1);
		addView(ll, llp);
	}

	private Button getButton()
	{
		Button button = new Button(getContext());
		button.setTextSize(mTextSize);
		button.setTextColor(Color.WHITE);
		button.setGravity(Gravity.CENTER);
		button.setFocusable(true);
		button.setFocusableInTouchMode(true);
		return button;
	}

	private TextView getTextView()
	{
		TextView tv = new TextView(getContext());
		tv.setTextSize(mTextSize);
		tv.setTextColor(Color.WHITE);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		return tv;
	}

	public void update(String[] strings)
	{
		statusStrings = strings;
		LogUtil.d(TAG, TAG + "  status:" + statusStrings);
	}

	@Override
	public void onClick(View v)
	{
		if (v.equals(mClose))
		{
			//关闭应用
			MainActivity.getInstance().finish();
		}else if(v.equals(mDisconnect)){
			//断开连接
			MainActivity.getInstance().retoLoginView();
		}
	}
}
