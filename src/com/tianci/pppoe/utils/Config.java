package com.tianci.pppoe.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.tianci.pppoe.PPPoEApplication;

public class Config
{
	public static float div = 1;
	public static int screenWidth = 1920;
	public static int screenHeight = 1080;

	public static float getDiv()
	{
		WindowManager wm = (WindowManager) PPPoEApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		screenWidth = point.x;
		screenHeight = point.y;
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
        return  div;
	}

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) PPPoEApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        return screenWidth;
    }

    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) PPPoEApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        return screenHeight;
    }
}
