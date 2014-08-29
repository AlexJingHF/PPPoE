package com.tianci.pppoe.layout;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import com.tianci.pppoe.utils.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alex on 14-8-27.
 */
public class CLoading extends SurfaceView implements SurfaceHolder.Callback{

//    private SurfaceHolder mHolder = null;

    private float div = Config.getDiv();

    private ExecutorService mEService = null;

    private Paint mPaintOuterCircle = null;
    private Paint mPaintInnerCircle = null;
    private Paint mPaintProgressCircle =null;
    private Paint mPaintProgressText = null;
    private Paint mPaintTipText = null;
    private Paint mPaintLoadingText = null;

    private static final int COLOR_WHITE = 0XFFFFFFFF;
    private static final int COLOR_WHITE_TRANSLATE = 0X7FFFFFFF;
    private static final int COLOR_TEXT_TIP = 0XFF54CCDC;
    private static final int COLOR_TEXT_LOADING = 0XFF1F81A6;
    //渐变色彩
    private static final int[] SWEEP_GRADIENT_COLORS = new int[] {
            0XFF9BCA41, 0XFF75C761, 0XFF1DBEAE, 0XFF11B5C8,0XFF2CABC8,
            0XFF5F9EC9, 0XFF9790C8, 0XFFE585BD, 0XFFFB8B9E,0XFFF99778,
            0XFFFAA94E, 0XFFF8B736, 0XFFE6BF36, 0XFFC7C735,0XFFA4CF3F,
            0XFF9BCA41 };
    private SweepGradient mSweepGradient = null;

    private float mCurrentProgress = 0;
    private float mProgressGonaBe = 0;

    private float mProgressWidth = 12f/div;
    private float mCenterX = 252f/div;
    private float mCenterY = 252f/div;
    private float mProgressTextY = 0f/div;
    private float mLoadingTextY = 0f/div;

    private float mOuterRadius = 252f/div;
    private float mInnerRadius = 180f/div;
    private float mProgressRadius = 105f/div;
    private float mTipRadius = 216f/div;

    private float mTipStartAngle = 0f;
    private float mTipSweepAngle = 0f;
    private float mProgressStartAngel = -90f;
    private float mProgressSweepAngle = 0f;

    private RectF mProgressRectF = null;

    private Path mTipPath = null;
    private RectF mTipRectF = null;
    private String mLoadingText = "Loading";
    private String mTipText = "Tip & Tip";


    private static final float TEXT_SIZE_PROGRESS = 60;
    private static final float TEXT_SIZE_LOADING = 40;

    private int FPS = 30;
    private int FPSTime = 33;

    private android.view.animation.Interpolator interpolator = null;

    public CLoadingListener getListener() {
        return listener;
    }

    public void setListener(CLoadingListener listener) {
        this.listener = listener;
    }

    private CLoadingListener listener = null;
    public interface CLoadingListener{
        public void onFinished();
    }

    public CLoading(Context context) {
        super(context);
        init();
    }

    public CLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setZOrderOnTop(true);   //设置成最底层图
        getHolder().setFormat(PixelFormat.TRANSLUCENT);//
        getHolder().addCallback(this);
        setLayoutParams(new ViewGroup.LayoutParams((int) (mOuterRadius * 2), (int) (mOuterRadius * 2)));
        mPaintErase.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaintErase.setColor(Color.TRANSPARENT);
        setBackgroundColor(Color.TRANSPARENT);
        initOutCircle();
        initInnerCircle();
        initProgressCircle();
        initProgressText();
        initTipText();
        initLoadingText();

        initPath();
        initProgressRactF();
        initInterpolator();
    }

    private void initOutCircle(){
        mPaintOuterCircle = new Paint();
        mPaintOuterCircle.setStyle(Paint.Style.FILL);
        mPaintOuterCircle.setAntiAlias(true);
        mPaintOuterCircle.setColor(COLOR_WHITE_TRANSLATE);
    }

    private void initInnerCircle(){
        mPaintInnerCircle = new Paint();
        mPaintInnerCircle.setStyle(Paint.Style.FILL);
        mPaintInnerCircle.setAntiAlias(true);
        mPaintInnerCircle.setColor(COLOR_WHITE);
    }

    private void initProgressCircle(){
        mPaintProgressCircle = new Paint();
        mPaintProgressCircle.setStyle(Paint.Style.STROKE);
        mPaintProgressCircle.setAntiAlias(true);
        initSweepGradient();
        mPaintProgressCircle.setShader(mSweepGradient);
        mPaintProgressCircle.setStrokeWidth(mProgressWidth);
    }

    private void initProgressText(){
        mPaintProgressText = new Paint();
        mPaintProgressText.setStyle(Paint.Style.FILL);
        mPaintProgressText.setAntiAlias(true);
        mPaintProgressText.setColor(COLOR_TEXT_TIP);
        mPaintProgressText.setTextAlign(Paint.Align.CENTER);
        mPaintProgressText.setTextSize(TEXT_SIZE_PROGRESS);
        mPaintProgressText.setTypeface(Typeface.MONOSPACE);
    }

    private void initTipText(){
        mPaintTipText = new Paint();
        mPaintTipText.setStyle(Paint.Style.FILL);
        mPaintTipText.setAntiAlias(true);
        mPaintTipText.setColor(COLOR_TEXT_TIP);
        mPaintTipText.setTextAlign(Paint.Align.CENTER);
        mPaintTipText.setTextSize(TEXT_SIZE_LOADING);
        mPaintTipText.setTypeface(Typeface.MONOSPACE);
    }

    private void initLoadingText(){
        mPaintLoadingText = new Paint();
        mPaintLoadingText.setStyle(Paint.Style.FILL);
        mPaintLoadingText.setAntiAlias(true);
        mPaintLoadingText.setColor(COLOR_TEXT_LOADING);
        mPaintLoadingText.setTextAlign(Paint.Align.CENTER);
        mPaintLoadingText.setTextSize(TEXT_SIZE_LOADING);
        mPaintLoadingText.setTypeface(Typeface.MONOSPACE);
    }

    private void initPath(){
        mTipPath = new Path();
        mTipRectF = new RectF();
        mTipRectF.set(mOuterRadius - mTipRadius, mOuterRadius - mTipRadius, mOuterRadius + mTipRadius, mOuterRadius + mTipRadius);
        mTipPath.addArc(mTipRectF, mTipStartAngle, 180);
    }

    private void initInterpolator(){
        interpolator = new DecelerateInterpolator();
    }

    private void initProgressRactF(){
        mProgressRectF = new RectF();
        mProgressRectF.set(mOuterRadius-mProgressRadius,mOuterRadius-mProgressRadius,mOuterRadius+mProgressRadius,mOuterRadius+mProgressRadius);
    }

    private void initSweepGradient(){
        mSweepGradient = new SweepGradient(mProgressRadius,mProgressRadius,SWEEP_GRADIENT_COLORS,null);
    }

    private void drawOuterCircle(Canvas canvas){
        canvas.drawCircle(mCenterX,mCenterY,mOuterRadius,mPaintOuterCircle);
    }

    private void drawInnerCircle(Canvas canvas){
        canvas.drawCircle(mCenterX,mCenterY,mInnerRadius,mPaintInnerCircle);
    }

    private void drawProgressCircle(Canvas canvas){
        canvas.drawArc(mProgressRectF, mProgressStartAngel, mProgressSweepAngle, false, mPaintProgressCircle);
    }

    private void drawProgressText(Canvas canvas){
        canvas.drawText((int)mDisplayProgress + "%", mCenterX, mCenterY, mPaintProgressText);
    }

    private void drawLoadingText(Canvas canvas){
        canvas.drawText(mLoadingText,mCenterX,mLoadingTextY,mPaintProgressText);
    }

    private void drawTipText(Canvas canvas){
        canvas.drawTextOnPath(mTipText,mTipPath,0,0,mPaintTipText);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        start();
    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public void start(){
        initData();
        initThread();
        isStop = false;
    }

    private void initData() {
        mCurrentProgress = 0f;
        mDisplayProgress = 0f;
        mProgressDiff = 0f;
        mProgressGonaBe = 0f;
        mProgressSweepAngle =0;
    }

    private void initThread(){
        mEService = Executors.newFixedThreadPool(5);
        mEService.execute(new ProgressCirclTask());
        mEService.execute(new TipTextTask());
        mEService.execute(new UIRefreshTask());
        mEService.shutdown();
    }

    private class UIRefreshTask implements Runnable{
        @Override
        public void run() {
            while (!isStop){
                try {
                Thread.sleep(FPSTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                Canvas canvas = getHolder().lockCanvas();
                if (canvas == null){
                    continue;
                }
//                erase(canvas);
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
                drawOuterCircle(canvas);
                drawInnerCircle(canvas);
                drawProgressCircle(canvas);
                drawProgressText(canvas);
                drawTipText(canvas);
                getHolder().unlockCanvasAndPost(canvas);

            }
        }
    }

    private class ProgressCirclTask implements Runnable{
        @Override
        public void run() {
            while(!isStop){
                mProgressSweepAngle = getCurrentProgress()*360/100f;
                if (mProgressSweepAngle == 360)
                    if (listener != null){
                        listener.onFinished();
                    }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private int mSpeedTip = 50;
    private class TipTextTask implements Runnable{
        @Override
        public void run() {
            while (!isStop){
                mTipStartAngle += 1;
                if (mTipStartAngle >=360){
                    mTipStartAngle -=360;
                }
                mTipPath.reset();
                mTipPath.addArc(mTipRectF, mTipStartAngle, 180);
              try {
                    Thread.sleep(mSpeedTip);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    Paint mPaintErase = new Paint();
    private void erase(Canvas canvas){
        canvas.drawPaint(mPaintErase);
    }

    private boolean isStop = false;
    public void stop(){
        if (!mEService.isShutdown()){
            mEService.shutdownNow();
        }
        isStop = true;
    }
    private float change = 0;
    private float mDisplayProgress = 0;
    private synchronized float getCurrentProgress(){
        if (mDisplayProgress == mProgressGonaBe){
            setmCurrentProgress(mDisplayProgress);
            return mDisplayProgress;
        }
        long currentTime = System.currentTimeMillis();
        float input = currentTime-mTimeProgressSet > 3000? 1.0f:(currentTime-mTimeProgressSet)/3000.0f;
        change = mProgressDiff*interpolator.getInterpolation(input);
        mDisplayProgress = getmCurrentProgress()+ change;
        change = 0;
        Log.d("jinghaifeng","currentProgress == "+mDisplayProgress);
        return mDisplayProgress;
    }

    private float mProgressDiff = 0f;
    private long mTimeProgressSet = 0;
    public synchronized void setProgress(int progress){
        mProgressGonaBe = progress;
        setmCurrentProgress(mDisplayProgress);
        mProgressDiff = mProgressGonaBe - getmCurrentProgress();
        mTimeProgressSet = System.currentTimeMillis();
    }

    public synchronized float getmCurrentProgress() {
        return mCurrentProgress;
    }

    public synchronized void setmCurrentProgress(float mCurrentProgress) {
        this.mCurrentProgress = mCurrentProgress;
    }
}
