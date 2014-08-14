package com.tianci.pppoe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tianci.pppoe.utils.LogUtil;

public class BaseActivity extends Activity
{
	private static final String TAG = BaseActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, TAG + "->onCreate");
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		LogUtil.d(TAG, TAG + "->onStart");
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		LogUtil.d(TAG, TAG + "->onStop");
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		LogUtil.d(TAG, TAG + "->onResume");
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		LogUtil.d(TAG, TAG + "->onDestroy");
	}

	protected void openActivity(Class<?> pClass)
	{
		openActivity(pClass, null);
	}

	protected void openActivity(Class<?> pClass, Bundle pBundle)
	{
		Intent intent = new Intent(this, pClass);
		if (pBundle != null)
		{
			intent.putExtras(pBundle);
		}
		LogUtil.d(TAG,
				TAG + "->onopenActivityStop->class==" + pClass.getSimpleName());
		startActivity(intent);
	}

	protected void openActivity(String pAction)
	{
		openActivity(pAction, null);
	}

	protected void openActivity(String pAction, Bundle pBundle)
	{
		Intent intent = new Intent(pAction);
		if (pBundle != null)
		{
			intent.putExtras(pBundle);
		}
		LogUtil.d(TAG, TAG + "->onopenActivityStop->action==" + pAction);
		startActivity(intent);
	}
}
