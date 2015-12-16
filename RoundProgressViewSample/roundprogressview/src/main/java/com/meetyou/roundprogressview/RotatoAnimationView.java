package com.meetyou.roundprogressview;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 圆形轨迹动画View
 * Created by lwh on 2015/12/16.
 */
public class RotatoAnimationView extends View {

    private static final String TAG = "RotatoAnimationView";
    private Context mContext;
    private Bitmap mBitmap;
    private int mBitmapWidth,mBitmapHeight;
    private int mViewWidth,mViewHeight;
    private int mBitmapResId;
    private int mAnimationRadius;
    private boolean mIsOneShot =false;
    private int mDuration;
    private Paint mPaint;

    //循环timer
    private Timer mTimer;
    //移动的x y
    private float mX,mY;
    //起始角度
    private double mSweepStartAngle;
    //间隔角度
    private double mSweepAngleInterval=5;


    public RotatoAnimationView(Context context) {
        super(context);
        initialView(context, null);
    }

    public RotatoAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialView(context, attrs);
    }

    public RotatoAnimationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialView(context,attrs);
    }

    protected void initialView(Context ctx, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = ctx.obtainStyledAttributes(attrs, R.styleable.RotatoAnimationView);
            mBitmapResId = array.getResourceId(R.styleable.RotatoAnimationView_icon, -1);
            mBitmap = BitmapFactory.decodeResource(getResources(), mBitmapResId);
            mBitmapWidth = mBitmap.getWidth();
            mBitmapHeight = mBitmap.getHeight();
            mIsOneShot=array.getBoolean(R.styleable.RotatoAnimationView_oneshot, false);
            mDuration = array.getInt(R.styleable.RotatoAnimationView_duration, 1000);
            array.recycle();
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mViewWidth==0 && mViewHeight==0){
            mViewWidth=getMeasuredWidth();
            mViewHeight  =getMeasuredHeight();
            start();
        }
    }

    /**
     * 停止动画
     */
    public void stop(){
        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
        mX =0;
        mY=mViewHeight/2-mBitmapHeight/2;
        invalidate();
    }
    /**
     * 启动动画
     */
    public void start(){
        if(mViewWidth==0 && mViewHeight==0){
            return;
        }
        mX =0;
        mY=mViewHeight/2-mBitmapHeight/2;
        changeValue();
        invalidate();

       /* RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(mDuration);
        animation.setRepeatMode(Animation.RESTART);
        startAnimation(animation);*/
    }

    //改变动画的值
    private void changeValue(){
        if(mTimer!=null){
            stop();
        }
        if(mTimer==null) {
            mTimer = new Timer();
        }
        //定时播放
        mSweepStartAngle=180;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //角度
                mSweepStartAngle-=mSweepAngleInterval;
                if(mSweepStartAngle==-180){
                    if(mIsOneShot){
                        mSweepStartAngle=180;
                        mHandler.sendEmptyMessage(STOP);
                        return;
                    }else{
                        mSweepStartAngle=180;
                    }
                }

                Message message = new Message();
                message.what =START;
                message.obj=mSweepStartAngle;
                mHandler.sendMessage(message);
            }
        },1,mDuration/(360/10));


        /*if(!mIsOneShot){
            mSweepStartAngle=180;
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //角度
                    mSweepStartAngle-=mSweepAngleInterval;
                    if(mSweepStartAngle==-180)
                        mSweepStartAngle=180;
                    Message message = new Message();
                    message.what =START;
                    message.obj=mSweepStartAngle;
                    mHandler.sendMessage(message);
                }
            },1,mDuration/(360/10));

            //mDuration为ms；一圈360度；希望执行次数为 每次转五° （360/5）,一次时间为mDuration/(360/5)
        }else{
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(START);
                }
            },1);
        }*/
    }

    private static final int START=1;
    private static final int STOP=2;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case START:{
                    //mX mY
                    float mCenterX = mX+mBitmapWidth/2;
                    float mCenterY = mY-mBitmapWidth/2;
                    float startX=0;
                    float startY=mViewHeight/2-mBitmapHeight/2;
                    //radius
                    mAnimationRadius = mViewWidth/2-mBitmapWidth/2;

                    //angle
                    double angle = (double)msg.obj;
                    //hudu
                    double hudu  = angle * Math.PI / 180; //换算成弧度

                    double x = Math.cos(hudu)*mAnimationRadius;
                    double y = Math.sin(hudu)*mAnimationRadius;

                    mX = (float)(mAnimationRadius+x);
                    mY = (float)(startY-y);

                    Log.d(TAG,"-->angle:"+angle+"--x:"+x+"  --y:"+y+"--mX:"+mX+"--mY:"+mY+"--radius:"+mAnimationRadius+"-- Math.cos(angle):"+ Math.cos(angle)+"--hudu:"+hudu+"");
                    invalidate();
                    break;
                }
                case STOP:{
                    stop();
                    break;
                }
                default:
                    break;
            }
        }
    };

    /*public void init(){
        mBitmap = BitmapFactory.decodeResource(getResources(),)
        int mDensity = (int) getResources().getDisplayMetrics().density;
        mPaintCircleOutside = new Paint();

        mPaintCircleOutside.setAntiAlias(true);
        mPaintCircleOutside.setStyle(Paint.Style.FILL);
        mPaintCircleOutside.setDither(true);

        mPaintCircleInside = new Paint();
        mPaintCircleInside.setAntiAlias(true);
        mPaintCircleInside.setStyle(Paint.Style.FILL);

        mPaintCircleCenter = new Paint();
        mPaintCircleCenter.setAntiAlias(true);
        mPaintCircleCenter.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaintCircleCenter.setStyle(Paint.Style.STROKE);
        mPaintCircleCenter.setStrokeJoin(Paint.Join.ROUND);
        //设置线的类型,边是圆的
        mPaintCircleCenter.setStrokeCap(Paint.Cap.ROUND);

        //内容
        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setFakeBoldText(true);
        if(roundProgressConfig.getTextColor()>0)
            mPaintText.setColor(getResources().getColor(roundProgressConfig.getTextColor()));
        if(roundProgressConfig.getTextSize()>0){
            mPaintText.setTextSize(roundProgressConfig.getTextSize()*mDensity);
        }
        //单位
        mPaintTextUnit = new Paint();
        mPaintTextUnit.setAntiAlias(true);
        mPaintTextUnit.setFlags(Paint.ANTI_ALIAS_FLAG);
        if(roundProgressConfig.getTextUnitColor()>0)
            mPaintTextUnit.setColor(getResources().getColor(roundProgressConfig.getTextUnitColor()));
        if(roundProgressConfig.getTextUnitSize()>0){
            mPaintTextUnit.setTextSize(roundProgressConfig.getTextUnitSize()*mDensity);
        }

        //起始偏移角度
        int a = roundProgressConfig.getProgressBarWidth()/2;
        int b = getRadius()-a;
        double hudu = Math.atan2(a,b);
        double jiaodu = hudu*180/Math.PI;
        mSweepStartAngle = 270+jiaodu;

        mShader = new SweepGradient(getWidth()/2,getHeight()/2,new int[]{  getResources().getColor(roundProgressConfig.getStartColor()),
                getResources().getColor(roundProgressConfig.getEndColor()),},null);

        *//* mShader = new RadialGradient(getWidth()/2,getHeight()/2,
                getRadius()(),getResources().getColor(roundProgressConfig.getStartColor()),
                getResources().getColor(roundProgressConfig.getEndColor()), Shader.TileMode.MIRROR);*//*
        *//*mShader = new LinearGradient(left, top, right, bottom,
                new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.LTGRAY*//**//*roundProgressConfig.getStartColor(),roundProgressConfig.getEndColor() *//**//*}, null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        *//*

        invalidate();
    }*/


    /**
     * 设置进度
     * @param progress
     * @param bAnimation 是否执行动画
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setProgress(int progress,boolean bAnimation){
      /*  if(roundProgressConfig.getMaxProgress()<progress || progress<roundProgressConfig.getMinProgress())
            return;

        if(!bAnimation){
            float percent = progress/(roundProgressConfig.getMaxProgress()*1.0f);
            mSweepAngle = 360 * percent;
            mText = progress+"";
            mProgress = progress;
        }else{
            mText = progress+"";
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0,progress);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int progress = (int)valueAnimator.getAnimatedValue();
                    mProgress = progress;
                    float percent = progress/(roundProgressConfig.getMaxProgress()*1.0f);
                    mSweepAngle = 360 * percent;
                    mText = progress+"";
                    invalidate();
                }
            });
            valueAnimator.setDuration(1000);
            valueAnimator.start();
        }*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);

    }

    private void drawView(Canvas canvas){
        canvas.drawBitmap(mBitmap,mX,mY,mPaint);

       /* //外圆
        float x = getWidth()/2;
        float y = getHeight()/2;
        int radus = getRadius();
        canvas.drawCircle(x, y, radus, mPaintCircleOutside);

        //内圆
        radus = radus-roundProgressConfig.getProgressBarWidth();
        canvas.drawCircle(x, y, radus, mPaintCircleInside);

        //内容长度
        Rect rectContent= new Rect();
        mPaintText.getTextBounds(mText, 0, mText.length(), rectContent);
        //单位长度
        Rect rectUnit= new Rect();
        mPaintTextUnit.getTextBounds(roundProgressConfig.getUnitText(), 0, roundProgressConfig.getUnitText().length(), rectUnit);
        //绘制内容
        x = getWidth()/2-(rectContent.width()+rectUnit.width())/2;
        y = getHeight()/2+rectContent.height()/2;
        canvas.drawText(mText, x, y, mPaintText);
        //绘制单位
        x = x+rectContent.width()+10;
        y = getHeight()/2+rectContent.height()/2;
        canvas.drawText(roundProgressConfig.getUnitText(), x, y, mPaintTextUnit);

        //旋转-100度，这是为了从底部开始画环形渐变；是-110而不是-90是因为drawArc要预留20来保留paint的round位置
        canvas.rotate(-100,getWidth()/2,getHeight()/2);
        if(mProgress==roundProgressConfig.getMaxProgress()){
            canvas.rotate(10,getWidth()/2,getHeight()/2);
        }
        //渐变圆
        int widthBolder = roundProgressConfig.getProgressBarWidth();
        int left = getWidth()/2-getRadius()+widthBolder/2;
        int top = getHeight()/2-getRadius()+widthBolder/2;
        int right = getWidth()/2+getRadius()-widthBolder/2;
        int bottom = getHeight()/2+getRadius()-widthBolder/2;
        RectF rectF = new RectF(left,top,right,bottom);
        mShader = new SweepGradient(getWidth()/2,getHeight()/2,new int[]{  getResources().getColor(roundProgressConfig.getStartColor()),
                getResources().getColor(roundProgressConfig.getEndColor()),},null);
        mPaintCircleCenter.setShader(mShader);

        canvas.drawArc(rectF, 20, mSweepAngle, false, mPaintCircleCenter);//144*/

    }

    private void drawCircle(Canvas canvas){

        /*//外圆
        float x = getWidth()/2;
        float y = getHeight()/2;
        int radus = getRadius();
        canvas.drawCircle(x, y, radus, mPaintCircleOutside);

        //内圆
        radus = radus-roundProgressConfig.getProgressBarWidth();
        canvas.drawCircle(x, y, radus, mPaintCircleInside);

        //内容长度
        Rect rectContent= new Rect();
        mPaintText.getTextBounds(mText, 0, mText.length(), rectContent);
        //单位长度
        Rect rectUnit= new Rect();
        mPaintTextUnit.getTextBounds(roundProgressConfig.getUnitText(), 0, roundProgressConfig.getUnitText().length(), rectUnit);
        //绘制内容
        x = getWidth()/2-(rectContent.width()+rectUnit.width())/2;
        y = getHeight()/2+rectContent.height()/2;
        canvas.drawText(mText, x, y, mPaintText);
        //绘制单位
        x = x+rectContent.width()+10;
        y = getHeight()/2+rectContent.height()/2;
        canvas.drawText(roundProgressConfig.getUnitText(), x, y, mPaintTextUnit);

        //旋转-100度，这是为了从底部开始画环形渐变；是-110而不是-90是因为drawArc要预留20来保留paint的round位置
        canvas.rotate(-100,getWidth()/2,getHeight()/2);
        if(mProgress==roundProgressConfig.getMaxProgress()){
            canvas.rotate(10,getWidth()/2,getHeight()/2);
        }
        //渐变圆
        int widthBolder = roundProgressConfig.getProgressBarWidth();
        int left = getWidth()/2-getRadius()+widthBolder/2;
        int top = getHeight()/2-getRadius()+widthBolder/2;
        int right = getWidth()/2+getRadius()-widthBolder/2;
        int bottom = getHeight()/2+getRadius()-widthBolder/2;
        RectF rectF = new RectF(left,top,right,bottom);
        mShader = new SweepGradient(getWidth()/2,getHeight()/2,new int[]{  getResources().getColor(roundProgressConfig.getStartColor()),
                getResources().getColor(roundProgressConfig.getEndColor()),},null);
        mPaintCircleCenter.setShader(mShader);

        canvas.drawArc(rectF, 20, mSweepAngle, false, mPaintCircleCenter);//144*/




    }


}
