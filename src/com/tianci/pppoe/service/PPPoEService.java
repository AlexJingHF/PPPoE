package com.tianci.pppoe.service;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Binder;
import android.os.IBinder;

import android.util.Log;
import com.skyworth.framework.skycommondefine.SkyConfigDefs;
import com.skyworth.framework.skysdk.ipc.SkyApplication;
import com.skyworth.framework.skysdk.ipc.SkyService;
import com.skyworth.framework.skysdk.plugins.SkyPluginParam;
import com.tianci.net.data.SkyPppoeInfo;
import com.tianci.pppoe.R;
import com.tianci.pppoe.utils.LogUtil;
import com.tianci.system.api.TCSystemService;
import com.tianci.system.data.TCInfoSetData;

public class PPPoEService extends IntentService implements SkyApplication.SkyCmdConnectorListener
{
	private static final String TAG = PPPoEService.class.getSimpleName();
    private TCSystemService mSystemApi = null;
	private PPPoEBinder mBinder = new PPPoEBinder();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PPPoEService(String name) {
        super(name);
    }

    public PPPoEService(){
        super("PPPoEService");
    }

    @Override
    public byte[] onHandler(String fromtarget, String cmd, byte[] body) {
        return new byte[0];
    }

    @Override
    public void onResult(String fromtarget, String cmd, byte[] body) {

    }

    @Override
    public String getCmdClassName() {
        return null;
    }

    /**
     * 概述：主要用于资源管理，当接收到该命令时，表示该应用从forground进入pause状态，请不要再显示loading环，清除菜单等操作<br/>
     *
     * @param fromtarget
     * @param cmd
     * @param body
     */
    @Override
    public byte[] requestPause(String fromtarget, String cmd, byte[] body) {
        return new byte[0];
    }

    /**
     * 概述：主要用于资源管理，当接收到该命令时，表示该应用从pause进入forground状态，需要重新注册菜单<br/>
     *
     * @param fromtarget
     * @param cmd
     * @param body
     */
    @Override
    public byte[] requestResume(String fromtarget, String cmd, byte[] body) {
        return new byte[0];
    }

    /**
     * 概述：主要用于资源管理，当接收到该命令时，需要应用释放自身占有的所有资源，消掉所有自身show的ui，并且退出<br/>
     *
     * @param fromtarget
     * @param cmd
     * @param body
     */
    @Override
    public byte[] requestRelease(String fromtarget, String cmd, byte[] body) {
        return new byte[0];
    }

    /**
     * 概述：TV必须实现，其他应用可不处理，当接收到该命令时，表示需要应用从release状态进入visibile状态，不需注册菜单<br/>
     *
     * @param fromtarget
     * @param cmd
     * @param body
     */
    @Override
    public byte[] requestStartToVisible(String fromtarget, String cmd, byte[] body) {
        return new byte[0];
    }

    /**
     * 概述：TV必须实现，其他应用可不处理，当接收到该命令时，表示需要该应用从release状态进入forground状态<br/>
     *
     * @param fromtarget
     * @param cmd
     * @param body
     */
    @Override
    public byte[] requestStartToForground(String fromtarget, String cmd, byte[] body) {
        return new byte[0];
    }

    public enum PPPoEStatus
	{
		CONNECTING, CONNECTED, DISCONNECTING, DISCONNECTED
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
        mSystemApi = new TCSystemService(this);
		// 检查当前连接状态

		mCurrentStatus = checkCurrentStatus();
		switch (mCurrentStatus)
		{
		case CONNECTED:
		{
			LogUtil.d(TAG, TAG + "->onCreate->CONNECTED ->OK SHUT DOWN");
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
		case CONNECTING:
			LogUtil.d(TAG, TAG + "->onCreate->CONNECTING");
            //先断开再连接
            disconnect();
            toConnect();
			break;
		case DISCONNECTING:
			LogUtil.d(TAG, TAG + "->onCreate->DISCONNECTING");
			break;
		default:
        {
            //未知状态则，直接尝试连接
            toConnect();
        }
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
        TCInfoSetData data = (TCInfoSetData) mSystemApi.getSetData(SkyConfigDefs.SKY_CFG_NETWORK_PPPOE_GET_STATE);
        String currentState = data.getCurrent();
        if (currentState.equals("CONNECTED")){
            LogUtil.d(TAG,TAG+" -> checkCurrentStatus -> CONNECTED ");
            return PPPoEStatus.CONNECTED;
        }else if (currentState.equals("DISCONNECTED")){
            LogUtil.d(TAG,TAG+" -> checkCurrentStatus -> DISCONNECTED ");
            return PPPoEStatus.DISCONNECTED;
        }else if (currentState.equals("CONNECTING")){
            LogUtil.d(TAG,TAG+" -> checkCurrentStatus -> CONNECTING ");
            return PPPoEStatus.CONNECTING;
        }

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
		mCurrentStatus = PPPoEStatus.DISCONNECTED;
        TCInfoSetData data3 = new TCInfoSetData();
        data3.setName(SkyConfigDefs.SKY_CFG_NETWORK_PPPOE_CONNECT);
        SkyPppoeInfo cInfo = new SkyPppoeInfo();
        cInfo.username = account;
        cInfo.password = psw;
        cInfo.netInterface = "eth0";
        SkyPluginParam params1 = new SkyPluginParam();
        data3.setCurrent(cInfo.toString());
        data3.setPluginValue(params1);
        mSystemApi.setData(data3);
        // 底层接口,建立线程尝试连接
        while (true){
            try {
                Thread.sleep(1000);
                if (checkCurrentStatus().equals(PPPoEStatus.CONNECTED)){
                    break;
                }
            } catch (InterruptedException e) {
                Log.e(TAG,TAG+" connect thread is interrupted");
                e.printStackTrace();
            }finally {

            }
        }
	}

	private void interruptConnect()
	{
		mCurrentStatus = PPPoEStatus.CONNECTING;
		// 底层接口，打断拨号连接
	}

	private void disconnect()
	{
		mCurrentStatus = PPPoEStatus.CONNECTED;
		// 底层接口，建立线程断开连接
        TCInfoSetData data = new TCInfoSetData();
        data.setName(SkyConfigDefs.SKY_CFG_NETWORK_PPPOE_DISCONNECT);
        data.setPluginValue(new SkyPluginParam());
        mSystemApi.setData(data);
    }

	@Override
	public IBinder onBind(Intent intent)
	{
		return mBinder;
	}

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               android.content.Context#startService(android.content.Intent)}.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        //开始拨号线程

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

//	private void refreshStatus(PPPoEStatus status, CallBackStatus callBackStatus)
//	{
//		mCurrentStatus = status;
//		if (mPPPoEStatusCallBacks != null && mPPPoEStatusCallBacks.size() > 0)
//		{
//			for (PPPoEStatusCallBack callBack : mPPPoEStatusCallBacks)
//			{
//				switch (callBackStatus)
//				{
//				case ONCONNECTING:
//				{
//					callBack.onConnecting(connectTimes, mCurrentStatus);
//				}
//					break;
//				case ONDISCONNECT:
//				{
//					callBack.onDisconnect(mCurrentStatus);
//				}
//					break;
//				case ONSTOPCONNECTING:
//				{
//					callBack.onStopConnecting(mCurrentStatus);
//				}
//					break;
//				default:
//					break;
//				}
//			}
//		}
//
//	}

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
