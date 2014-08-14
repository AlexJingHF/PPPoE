package com.tianci.pppoe.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tianci.pppoe.R;
import com.tianci.pppoe.layout.CircleLoading;
import com.tianci.pppoe.layout.CircleLoading.CircleLoadingListener;
import com.tianci.pppoe.layout.LoginView;
import com.tianci.pppoe.layout.StatusView;
import com.tianci.pppoe.service.PPPoEService;
import com.tianci.pppoe.service.PPPoEService.PPPoEBinder;
import com.tianci.pppoe.utils.Config;
import com.tianci.pppoe.utils.LogUtil;
import com.tianci.pppoe.view.BgScrollView;
import com.tianci.pppoe.view.BgScrollView.ScrollStatusListener;

public class MainActivity extends BaseActivity implements
		CircleLoadingListener, ScrollStatusListener
{
	private static final String TAG = MainActivity.class.getSimpleName();
	private PPPoEService.PPPoEBinder mBinder = null;

	private RelativeLayout mMainLayout = null;
	private LinearLayout mLinearLayout = null;
	private CircleLoading mLoading = null;
	private BgScrollView mScrollView = null;
	private LoginView mLoginView = null;
	private StatusView mStatusView = null;

	private RelativeLayout.LayoutParams mCenterParams;

	private float mScreenWidth = 1920f;
	private float mScreenHeight = 1080f;

	private int mLoginPaddingTop = 99;
	private int mStatusPaddingTop = 510;
	private float div = 1.5f;
	private static MainActivity INSTANCE = null;
	private int mStep = 1;

	private LayoutAnimationController lac = null;
	private ServiceConnection mServiceConnection = new ServiceConnection()
	{

		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			Log.d(TAG, TAG + "->onServiceDisconnected");
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			Log.d(TAG, TAG + "->onServiceConnected");
			mBinder = (PPPoEBinder) service;
		}
	};


	private enum INITTYPE
	{
		COM_INIT, UI_INIT
	}

	private class InitInTurns
	{
		byte[] turns = { 0, 0 };

		public void notifyInit(INITTYPE type)
		{
			switch (type)
			{
			case COM_INIT:
				turns[0] = 1;
				break;
			case UI_INIT:
				turns[1] = 1;
				break;
			default:
				break;
			}
		}

		public boolean isTurn()
		{
			return turns[0] == 1 && turns[1] == 1;
		}
	}

	private InitInTurns mInitInTurns = new InitInTurns();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		INSTANCE = this;
		div = Config.getInstance(this).div;
		Intent service = new Intent(this, PPPoEService.class);
		bindService(service, mServiceConnection, BIND_AUTO_CREATE);
		mMainLayout = new RelativeLayout(this);
		mMainLayout.setLayoutParams(new ViewGroup.LayoutParams(
				(int) (mScreenWidth / div), (int) (mScreenHeight / div)));
		mMainLayout.setGravity(Gravity.CENTER);
		mCenterParams = new RelativeLayout.LayoutParams(-1, -1);
		mLinearLayout = new LinearLayout(this);
		mLinearLayout.setGravity(Gravity.CENTER);
		mLinearLayout.setLayoutParams(mCenterParams);
		mMainLayout.addView(mLinearLayout);

		setContentView(mMainLayout);
		mLoading = new CircleLoading(this, div);
		mLinearLayout.addView(mLoading);
		new UIBuildThread().execute();
	}
	
	
	@Override
	protected void onStart()
	{
		super.onStart();
		mLoading.setCircleLoadingListener(this);
		mLoading.setTip("程序初始化中...");
		mLoading.start(2000);
	}

	private final class UIBuildThread extends AsyncTask<Void, Integer, Boolean>
	{
		
		@Override
		protected void onPreExecute()
		{
			mLoginView = new LoginView(MainActivity.this, div);
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(Void... params)
		{
			mScrollView = new BgScrollView(MainActivity.this, div);
			mScrollView.setScrollStatusListener(MainActivity.this);
//			isInit = true;
			mInitInTurns.notifyInit(INITTYPE.UI_INIT);
			if (mInitInTurns.isTurn())
			{
				toLoginView();
			}
			mStatusView = new StatusView(MainActivity.this, div);
			return true;
		}
	}

//	private boolean isInit = false;
//	private boolean isProgressed = false;

	private void toLoginView()
	{
			mHandler.sendEmptyMessage(1);
	}

	public void retoLoginView()
	{
		mScrollView.getScroller().startScroll(0, (int) (1840 / div), 0,
				(int) (-2000 / div), 2000);
//		(int) (-1840 / div), 2000);
		mScrollView.computeScroll();
		mHandler.sendEmptyMessage(6);
		// mHandler.sendEmptyMessage(4);
		new Thread()
		{
			public void run()
			{
				while (mScrollView.getScroller().computeScrollOffset())
				{

				}
				mHandler.sendEmptyMessage(1);
			};
		}.start();
	}

	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)
			{
			case 1:
			{
				mStep = 1;
				LogUtil.d("mHandler", "1");
				mMainLayout.removeAllViews();
				mLinearLayout.removeAllViews();
				mLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
				mMainLayout.addView(mScrollView);
				mMainLayout.addView(mLinearLayout);
				mLoginView.setY(mLoginPaddingTop / div);
				mLinearLayout.addView(mLoginView);
				mLinearLayout.setLayoutAnimation(getLacFadeIn());
				mLoginView.requestFocus();
			}
				break;
			case 2:
			{
				mScrollView.getScroller().startScroll(0, 0, 0,
						(int) (796 / div), 2000);
				mScrollView.computeScroll();
				mStep = 2;
				mLinearLayout.removeAllViews();
				mLinearLayout.setGravity(Gravity.CENTER);
				mLinearLayout.setLayoutAnimation(getLacScaleIn());
				mLoading.stop();
				mLoading.setTip("下班了 吃饭了 ...");
				mLinearLayout.addView(mLoading);
				mLoading.start(5000);
			}
				break;
			case 3:
			{
				// mScrollView.getScroller().startScroll(0, (int) (796 / div),
				// 0,
				// (int) (1042 / div), 2000);
				// mScrollView.computeScroll();
				mStep = 3;
				mLinearLayout.removeAllViews();
				mLoading.stop();
				mLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
				mLinearLayout.setLayoutAnimation(getLacFadeIn());
				mStatusView.setY(mStatusPaddingTop / div);
				mLinearLayout.addView(mStatusView);
				mStatusView.requestFocus();
			}
				break;
			case 4:
			{
				mStep = 1;
				mMainLayout.removeAllViews();
				mLinearLayout.removeAllViews();
				mLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
				mMainLayout.addView(mScrollView);
				mMainLayout.addView(mLinearLayout);
				mLoginView.setY(mLoginPaddingTop / div);
				mLinearLayout.addView(mLoginView);
				mLoginView.requestFocus();
			}
				break;
			case 5:
			{
				Animation anim = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.trans_out);
				anim.setAnimationListener(new AnimationListener()
				{

					@Override
					public void onAnimationStart(Animation animation)
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation)
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation)
					{
						mLinearLayout.removeAllViews();
					}
				});
				mLoading.startAnimation(anim);
			}
				break;
			case 6:
			{
				Animation anim = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.fade_out);
				anim.setAnimationListener(new AnimationListener()
				{

					@Override
					public void onAnimationStart(Animation animation)
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation)
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation)
					{
						mLinearLayout.removeAllViews();
						mStep = 1;
					}
				});
				mStatusView.startAnimation(anim);
			}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unbindService(mServiceConnection);
	}

	@Override
	public void onFinished(boolean isFinish)
	{
		switch (mStep)
		{
		case 1:
			mInitInTurns.notifyInit(INITTYPE.COM_INIT);
			if (mInitInTurns.isTurn())
			{
				toLoginView();
			}
			break;
		case 2:
			mScrollView.getScroller().startScroll(0, (int) (796 / div), 0,
					(int) (1044 / div), 2000);
			mScrollView.computeScroll();
			// toStatusView();
			mHandler.sendEmptyMessage(5);
			new Thread()
			{
				public void run()
				{
					while (mScrollView.getScroller().computeScrollOffset())
					{

					}
					toStatusView();
				};
			}.start();
			break;
		case 3:
		{
			mHandler.sendEmptyMessage(6);
		}
			break;
		default:
			break;
		}

	}

	private void toStatusView()
	{
		mHandler.sendEmptyMessage(3);
	}

	@Override
	public void onCurrentProgress(int progress)
	{

	}

	public void toConnectingLoading()
	{
		mHandler.sendEmptyMessage(2);
	}

	public static MainActivity getInstance()
	{
		return INSTANCE;
	}

	private LayoutAnimationController getLacFadeIn()
	{

		lac = new LayoutAnimationController(AnimationUtils.loadAnimation(this,
				R.anim.fade_in));
		return lac;
	}

	private LayoutAnimationController getLacScaleIn()
	{

		lac = new LayoutAnimationController(AnimationUtils.loadAnimation(this,
				R.anim.scale_in));
		return lac;
	}

	@Override
	public void onScrollFinished(boolean isFinished)
	{
		if (!isFinished)
		{
			return;
		}
		LogUtil.d("onScrollFinished", "status = " + mStep
				+ "  onScrollFinished == " + isFinished);
		switch (mStep)
		{
		case 3:
			// toStatusView();
			break;
		case 1:
		{
			// mHandler.sendEmptyMessage(1);
		}
		default:
			break;
		}
	}

}
