package com.tianci.pppoe.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class Config
{
	public float div = 1;
	public int screenWidth = 1920;
	public int screenHeight = 1080;
	
	private static Config INSTANCE = null;
	
	private Config(Context context)
	{
		init(context);
	}
	
	public static Config getInstance(Context context)
	{
		if (INSTANCE == null)
		{
			synchronized (Config.class)
			{
				if (null == INSTANCE)
				{
					INSTANCE = new Config(context);
					return INSTANCE;
				}
			}
		}
		return INSTANCE;
	}
	
	private void init(Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		screenWidth = point.x;
		screenHeight = point.y;
		Log.d("jinghaifeng", "w =="+screenWidth);
		switch (screenWidth)
		{
		case 1920:
			div = 1f;
			break;
		case 1280:
		{
			div = 1.5f;
		}
			break;
		default:
			div = 1.5f;
			break;
		}
	}
}
