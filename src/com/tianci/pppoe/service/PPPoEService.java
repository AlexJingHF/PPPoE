package com.tianci.pppoe.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Binder;
import android.os.IBinder;

import com.tianci.pppoe.R;
import com.tianci.pppoe.utils.LogUtil;

public class PPPoEService extends Service
{
	private static final String TAG = PPPoEService.class.getSimpleName();

	private PPPoEBinder mBinder = new PPPoEBinder();

	public enum PPPoEStatus
	{
		CONNECT, CONNECTED, DISCONNECT, DISCONNECTED
	}

	public enum CallBackStatus
	{
		ONCONNECTING, ONDISCONNECT, ONSTOPCONNECTING
	}

	private PPPoEStatus mCurrentStatus = PPPoEStatus.DISCONNECTED;

	public interface PPPoEStatusCallBack
	{
		public void onConnecting(int time, PPPoEStatus status);

		public void onDisconnect(PPPoEStatus status);

		public void onStopConnecting(PPPoEStatus status);
	}

	private List<PPPoEStatusCallBack> mPPPoEStatusCallBacks = new ArrayList<PPPoEService.PPPoEStatusCallBack>();

	private int connectTimes = 0;

	@Override
	public void onCreate()
	{
		super.onCreate();
		LogUtil.d(TAG, TAG + "->onCreate");
		// 检查当前连接状态
		mCurrentStatus = checkCurrentStatus();
		switch (mCurrentStatus)
		{
		case CONNECTED:
		{
			LogUtil.d(TAG, TAG + "->onCreate->CONNECTED");
		}
			break;
		case DISCONNECTED:
		{
			LogUtil.d(TAG, TAG + "->onCreate->DISCONNECTED");
			if (isAutoConnect())
			{
				toConnect();
			}
		}
			break;
		case CONNECT:
			LogUtil.d(TAG, TAG + "->onCreate->CONNECT");
			break;
		case DISCONNECT:
			LogUtil.d(TAG, TAG + "->onCreate->DISCONNECT");
			break;
		default:
			break;
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		LogUtil.d(TAG, TAG + "->onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		LogUtil.d(TAG, TAG + "->onDestroy");
	}

	private PPPoEStatus checkCurrentStatus()
	{
		// 底层plugin接口
		return PPPoEStatus.DISCONNECTED;
	}

	private boolean isAutoConnect()
	{
		Resources res = getResources();
		SharedPreferences sharedPreferences = getSharedPreferences(
				res.getString(R.string.pppoe_shared_preferences), MODE_PRIVATE);
		String account = sharedPreferences.getString(
				res.getString(R.string.pppoe_shared_preferences_account), "");
		String psw = sharedPreferences.getString(
				res.getString(R.string.pppoe_shared_preferences_pwd), "");
		boolean auto = sharedPreferences.getBoolean(
				res.getString(R.string.pppoe_shared_preferences_auto_connect),
				false);
		if (auto && !account.equals("") && !psw.equals(""))
		{
			LogUtil.d(TAG, TAG + "->isAutoConnect->true");
			return true;
		} else
		{
			LogUtil.d(TAG, TAG + "->isAutoConnect->false");
			return false;
		}
	}

	private void toConnect()
	{
		Resources res = getResources();
		SharedPreferences sharedPreferences = getSharedPreferences(
				res.getString(R.string.pppoe_shared_preferences), MODE_PRIVATE);
		String account = sharedPreferences.getString(
				res.getString(R.string.pppoe_shared_preferences_account), "");
		String psw = sharedPreferences.getString(
				res.getString(R.string.pppoe_shared_preferences_pwd), "");

		connect(account, psw);
	}

	private void connect(String account, String psw)
	{
		mCurrentStatus = PPPoEStatus.CONNECT;
		// 底层接口,建立线程尝试连接
	}

	private void interruptConnect()
	{
		mCurrentStatus = PPPoEStatus.DISCONNECT;
		// 底层接口，打断拨号连接
	}

	private void disconnect()
	{
		mCurrentStatus = PPPoEStatus.DISCONNECT;
		// 底层接口，建立线程断开连接
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return mBinder;
	}

	public void setPPPoEStatusCallBack(PPPoEStatusCallBack callBack)
	{
		if (callBack == null)
		{
			return;
		}
		if (mPPPoEStatusCallBacks != null
				&& !mPPPoEStatusCallBacks.contains(callBack))
		{
			mPPPoEStatusCallBacks.add(callBack);
		}
	}

	private void refreshStatus(PPPoEStatus status, CallBackStatus callBackStatus)
	{
		mCurrentStatus = status;
		if (mPPPoEStatusCallBacks != null && mPPPoEStatusCallBacks.size() > 0)
		{
			for (PPPoEStatusCallBack callBack : mPPPoEStatusCallBacks)
			{
				switch (callBackStatus)
				{
				case ONCONNECTING:
				{
					callBack.onConnecting(connectTimes, mCurrentStatus);
				}
					break;
				case ONDISCONNECT:
				{
					callBack.onDisconnect(mCurrentStatus);
				}
					break;
				case ONSTOPCONNECTING:
				{
					callBack.onStopConnecting(mCurrentStatus);
				}
					break;
				default:
					break;
				}
			}
		}

	}

	public void removePPPoEStatusCallBack(PPPoEStatusCallBack callBack)
	{
		if (callBack == null)
		{
			return;
		}
		if (mPPPoEStatusCallBacks != null
				&& mPPPoEStatusCallBacks.contains(callBack))
		{
			mPPPoEStatusCallBacks.remove(callBack);
		}
	}

	public class PPPoEBinder extends Binder
	{
		public void startConnecting()
		{
			LogUtil.d(TAG, TAG + "->startConnecting");
			toConnect();
		}

		public void stopConnecting()
		{
			LogUtil.d(TAG, TAG + "->stopConnecting");
			interruptConnect();
		}

		public void toDisconnect()
		{
			LogUtil.d(TAG, TAG + "->disconnect");
			disconnect();
		}

		public PPPoEStatus getStatus()
		{
			LogUtil.d(TAG, TAG + "->checkStatus");
			return mCurrentStatus;
		}

		public void setStatusCallBack(PPPoEStatusCallBack callBack)
		{
			setPPPoEStatusCallBack(callBack);
		}

		public void removeStatusCallBack(PPPoEStatusCallBack callBack)
		{
			removePPPoEStatusCallBack(callBack);
		}
	}

}
