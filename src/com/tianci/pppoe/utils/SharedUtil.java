package com.tianci.pppoe.utils;

import com.tianci.pppoe.R;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedUtil
{
	public static void setUserAccount(String account,Context context)
	{
		String name = context.getResources().getString(R.string.pppoe_shared_preferences);
		SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		String key = context.getResources().getString(R.string.pppoe_shared_preferences_account);
		sharedPreferences.edit().putString(key, account);
		sharedPreferences.edit().commit();
	}
	
	public static void setUserPsw(String psw,Context context)
	{
		String name = context.getResources().getString(R.string.pppoe_shared_preferences);
		SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		String key = context.getResources().getString(R.string.pppoe_shared_preferences_pwd);
		sharedPreferences.edit().putString(key, psw);
		sharedPreferences.edit().commit();
	}
	
	public static void setUserInterface(String netInterface,Context context)
	{
		String name = context.getResources().getString(R.string.pppoe_shared_preferences);
		SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		String key = context.getResources().getString(R.string.pppoe_shared_preferences_interface);
		sharedPreferences.edit().putString(key, netInterface);
		sharedPreferences.edit().commit();
	}
	
	public static void setUserAutoConnect(boolean isAuto,Context context)
	{
		String name = context.getResources().getString(R.string.pppoe_shared_preferences);
		SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		String key = context.getResources().getString(R.string.pppoe_shared_preferences_auto_connect);
		sharedPreferences.edit().putBoolean(key, isAuto);
		sharedPreferences.edit().commit();
	}
	
	
}
