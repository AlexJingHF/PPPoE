package com.tianci.pppoe.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tianci.pppoe.service.PPPoEService;
import com.tianci.pppoe.utils.LogUtil;

public class BootBroadcastReceiver extends BroadcastReceiver
{
	private static final String TAG = BootBroadcastReceiver.class.getSimpleName();
	@Override
	public void onReceive(Context context, Intent intent)
	{
		LogUtil.d(TAG, TAG+"->onReceive->action=="+intent.getAction());
		Intent i = new Intent(context, PPPoEService.class);
		context.startService(i);
	}

}
