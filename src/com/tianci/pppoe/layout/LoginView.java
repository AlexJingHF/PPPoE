package com.tianci.pppoe.layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianci.pppoe.R;
import com.tianci.pppoe.activity.MainActivity;
import com.tianci.pppoe.utils.SharedUtil;
import com.tianci.pppoe.view.EditTextWithImg;
import com.tianci.pppoe.view.SwicthTextView;

public class LoginView extends LinearLayout implements OnClickListener
{
	private static final String TAG = LoginView.class.getSimpleName();

	public static int mWidth = 625;
	private int mHeight = -2;
	private float div = 1;
	private EditTextWithImg mAccuntEdit = null;
	private EditTextWithImg mPswEdit = null;

	private CheckBox mShowPsw = null;
	private CheckBox mAutoConnect = null;
	private SwicthTextView mSwicthTextView = null;
	private Button mButtonConnect = null;
	
	private TextView mTitle = null;
	private float mTitleSize = 70;
	private float mSmallTitle = 50;
	private float mTextSize = 40;
	
	public LoginView(Context context, float div)
	{
		super(context);
		this.div = div;
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER_HORIZONTAL);
		setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
		initUI();
		addUI();
		addData();
	}

	private void addData()
	{
		String name = getResources().getString(R.string.pppoe_shared_preferences);
		SharedPreferences sharedPreferences = getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
		String account = sharedPreferences.getString(getResources().getString(R.string.pppoe_shared_preferences_account), "");
		if (!account.equals(""));
		{
			String psw = sharedPreferences.getString(getResources().getString(R.string.pppoe_shared_preferences_pwd), "");
			boolean isauto = sharedPreferences.getBoolean(getResources().getString(R.string.pppoe_shared_preferences_auto_connect), false);
			String netInterface = sharedPreferences.getString(getResources().getString(R.string.pppoe_shared_preferences_interface), "");
			
			mAccuntEdit.setText(account);
			mPswEdit.setText(psw);
			mAutoConnect.setChecked(isauto);
		}
	}

	private void addUI()
	{
		addTitle();
		addAccunt();
		addPsw();
		addCheckbox();
		addSwitchText();
		addButton();
	}

	private void addButton()
	{		
		LinearLayout ll = new LinearLayout(getContext());
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		ll.setPadding(0, (int) (36/div), 0, 0);
		ll.addView(mButtonConnect);
		addView(ll);
	}

	private void addPsw()
	{		
		LinearLayout ll = new LinearLayout(getContext());
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		ll.setPadding(0, (int) (30/div), 0, 0);
		ll.addView(mPswEdit);
		addView(ll);
	}

	private void addAccunt()
	{		
		LinearLayout ll = new LinearLayout(getContext());
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		ll.setPadding(0, (int) (45/div), 0, 0);
		ll.addView(mAccuntEdit);
		addView(ll);
	}

	private void addTitle()
	{		
		LinearLayout ll = new LinearLayout(getContext());
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		ll.addView(mTitle);
		addView(ll);
	}

	private void addSwitchText()
	{
		LinearLayout ll = new LinearLayout(getContext());
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		ll.setPadding(0, (int) (24/div), 0, 0);
		TextView tv = new TextView(getContext());
		tv.setTextColor(Color.WHITE);
		tv.setTextSize(mSmallTitle / div);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setText(R.string.network_interface_title);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
				(int) (231 / div), (int) (105 / div));
		ll.addView(tv, llp);
		ll.addView(mSwicthTextView);
		addView(ll);
	}

	private void addCheckbox()
	{
		RelativeLayout lbox = new RelativeLayout(getContext());
		lbox.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams lboxParams = new LinearLayout.LayoutParams(
				-1, -2);
		addView(lbox, lboxParams);
		lbox.setPadding(0, (int) (33/div), 0, 0);
		
		RelativeLayout.LayoutParams lPswParams = new RelativeLayout.LayoutParams(
				-2, -2);
		lPswParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		mShowPsw = new CheckBox(getContext());
		mShowPsw.setText(R.string.show_psw);
		mShowPsw.setTextSize(mTextSize / div);
		mShowPsw.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		mShowPsw.setId(0x000001);
		mShowPsw.setButtonDrawable(R.drawable.check_box_selector);
		lbox.addView(mShowPsw, lPswParams);
		mShowPsw.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked)
			{
				if (isChecked)
				{
					mPswEdit.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				} else
				{
					mPswEdit.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				}
			}
		});

		RelativeLayout.LayoutParams lAutoParams = new RelativeLayout.LayoutParams(
				-2, -2);
		lAutoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.TRUE);
		mAutoConnect = new CheckBox(getContext());
		mAutoConnect.setText(R.string.auto_connect);
		mAutoConnect.setTextSize(mTextSize / div);
		mAutoConnect.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		mAutoConnect.setButtonDrawable(R.drawable.check_box_selector);
		mAutoConnect.setId(0x000002);
		lbox.addView(mAutoConnect, lAutoParams);
		
		mShowPsw.setNextFocusRightId(mAutoConnect.getId());
		mAutoConnect.setNextFocusLeftId(mShowPsw.getId());
		
	}

	private void initUI()
	{
		mTitle = new TextView(getContext());
		mTitle.setTextColor(Color.WHITE);
		mTitle.setTextSize(mTitleSize / div);
		mTitle.setGravity(Gravity.CENTER);
		mTitle.setText(R.string.pppoe_title);

		mAccuntEdit = new EditTextWithImg(getContext(), div);
		mAccuntEdit.setTextColor(Color.WHITE);
		mAccuntEdit.setTextSize(mTextSize / div);
		mAccuntEdit.setIcon(R.drawable.user_icon);
		mAccuntEdit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(23)});
		mAccuntEdit.setSingleLine();

		mPswEdit = new EditTextWithImg(getContext(), div);
		mPswEdit.setIcon(R.drawable.psw_icon);
		mPswEdit.setTextColor(Color.WHITE);
		mPswEdit.setTextSize(mTextSize / div);
		mPswEdit.setSingleLine();
		mPswEdit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(23)});
		mPswEdit.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		
		mSwicthTextView = new SwicthTextView(getContext(), div);
		
		mButtonConnect = new Button(getContext());
		mButtonConnect.setTextSize(mSmallTitle);
		mButtonConnect.setTextColor(Color.WHITE);
		mButtonConnect.setText(R.string.connect);
		mButtonConnect.setBackgroundResource(R.drawable.button_selector);
		mButtonConnect.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v.equals(mButtonConnect));
		{
			//检查当前状态
			//判断是否拨号
			//进入拨号状态
			if (check())
			{
				saveData();
				MainActivity.getInstance().toConnectingLoading();
			}
		}
	}
	
	private void saveData()
	{
		SharedUtil.setUserAccount(mAccuntEdit.getText().toString(), getContext());
		SharedUtil.setUserPsw(mPswEdit.getText().toString(), getContext());
		SharedUtil.setUserInterface(mSwicthTextView.getCurrentString(), getContext());
		SharedUtil.setUserAutoConnect(mAutoConnect.isChecked(), getContext());
	}
	
	public boolean check()
	{
		if (mAccuntEdit.getText().equals("") || mPswEdit.equals(""))
		{
			Toast.makeText(getContext(), "请检查账号或密码是否输入", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

}
