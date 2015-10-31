package com.meetyou.roundprogressview;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * 环形进度条
 * Created by lwh on 2015/10/15.
 */
public class RoundProgressView extends View {

    private Context mContext;
    private RoundProgressConfig roundProgressConfig;

    //内外圆形
    private Paint  mPaintCircleOutside;
    private Paint mPaintCircleInside;
    //中间扇形
    private Paint mPaintCircleCenter;
    //内容画笔
    private Paint mPaintText;
    //单位画笔
    private Paint mPaintTextUnit;
    //动画角度
    private float mSweepAngle;
    //当前进度
    private int mProgress;
    //起始角度
    private double mSweepStartAngle;


     private String mText;

    private RectF rectF;
    private Shader mShader;
    public RoundProgressView(Context context) {
        super(context);
        mContext = context;
    }

    public RoundProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public RoundProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    private int getRadius(){
        return getWidth()/2;
    }

    public void init(RoundProgressConfig config){
        roundProgressConfig = config;
        int mDensity = (int) getResources().getDisplayMetrics().density;
        mPaintCircleOutside = new Paint();
        if (config.getCircleBgColor() > 0)
            mPaintCircleOutside.setColor(getResources().getColor(config.getCircleBgColor()));
        mPaintCircleOutside.setAntiAlias(true);
        mPaintCircleOutside.setStyle(Paint.Style.FILL);
        mPaintCircleOutside.setDither(true);

        mPaintCircleInside = new Paint();
        if (config.getCircleInsideColor() > 0)
            mPaintCircleInside.setColor(getResources().getColor(config.getCircleInsideColor()));
        mPaintCircleInside.setAntiAlias(true);
        mPaintCircleInside.setStyle(Paint.Style.FILL);

        mPaintCircleCenter = new Paint();
        mPaintCircleCenter.setAntiAlias(true);
        mPaintCircleCenter.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaintCircleCenter.setStyle(Paint.Style.STROKE);
        mPaintCircleCenter.setStrokeJoin(Paint.Join.ROUND);
        //设置线的类型,边是圆的
        mPaintCircleCenter.setStrokeCap(Paint.Cap.ROUND);
        mPaintCircleCenter.setStrokeWidth(roundProgressConfig.getProgressBarWidth());

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

        /* mShader = new RadialGradient(getWidth()/2,getHeight()/2,
                getRadius()(),getResources().getColor(roundProgressConfig.getStartColor()),
                getResources().getColor(roundProgressConfig.getEndColor()), Shader.TileMode.MIRROR);*/
        /*mShader = new LinearGradient(left, top, right, bottom,
                new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.LTGRAY*//*roundProgressConfig.getStartColor(),roundProgressConfig.getEndColor() *//*}, null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        */

        invalidate();
    }

    public RoundProgressConfig getRoundProgressConfig(){
        return roundProgressConfig;
    }

    public void update(){
        invalidate();
    }

    /**
     * 设置进度
     * @param progress
     * @param bAnimation 是否执行动画
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setProgress(int progress,boolean bAnimation){
        if(roundProgressConfig.getMaxProgress()<progress || progress<roundProgressConfig.getMinProgress())
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
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画内外圆
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas){

        //外圆
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

        canvas.drawArc(rectF, 20, mSweepAngle, false, mPaintCircleCenter);//144




    }


}
