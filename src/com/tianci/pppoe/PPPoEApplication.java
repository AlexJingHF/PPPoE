package com.tianci.pppoe;

import com.tianci.pppoe.service.PPPoEService;
import com.tianci.pppoe.utils.LogUtil;

import android.app.Application;
import android.content.Intent;

public class PPPoEApplication extends Application
{
	private static final String TAG = PPPoEApplication.class.getSimpleName();
	@Override
	public void onCreate()
	{
		super.onCreate();
		LogUtil.d(TAG, TAG+"->onCreate");
		startService(new Intent(this, PPPoEService.class));
	}
	
}
