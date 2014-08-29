package com.tianci.pppoe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.skyworth.framework.skysdk.ipc.SkyActivity;
import com.tianci.pppoe.utils.LogUtil;

public class BaseActivity extends SkyActivity
{
	private static final String TAG = BaseActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, TAG + "->onCreate");
	}

    /**
     * 概述：activity在命令通讯接口准备完毕的时候会回调该接口
     * 适用条件：任何继承了SkyActivity的子类
     * 执行流程：需要集成酷开系统的命令通讯方法
     * 注意事项：1，该接口在整个程序生命周期中只调用一次。
     * 2，在activity生命周期中，进入pause或者stop状态的时候，不会再收发到cmd，但是resume或者restart的时候
     * 会重新具备收发命令的能力，这个时候是不会重新回调该接口的。
     *
     * @date 2013-10-29
     */
    @Override
    public void onCmdConnectorInit() {

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

    @Override
    public byte[] onHandler(String fromtarget, String cmd, byte[] body) {
        return new byte[0];
    }

    @Override
    public void onResult(String fromtarget, String cmd, byte[] body) {

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
}
