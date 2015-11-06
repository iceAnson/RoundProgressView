package com.meetyou.roundprogressview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by lwh on 2015/11/5.
 */
public class ArcTextView extends View {

    private Context mContext;
    //内容画笔
    private Paint mPaintText;
    public ArcTextView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ArcTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ArcTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

    }


    public void init() {
        //内容
        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setFlags(Paint.ANTI_ALIAS_FLAG);
        //mPaintText.setColor(());
    }

    private String mText="";
    private int mRadius = 20;
    private int mColor;
    private int mTextSize;
    public void setText(String text,int textSize,int radius,int color){
        int mDensity = (int) getResources().getDisplayMetrics().density;
        mText = text;
        mColor = getResources().getColor(color);
        mTextSize = textSize*mDensity;
        mRadius = radius;
        mPaintText.setColor(mColor);
        mPaintText.setTextSize(mTextSize);
        mPaintText.setStyle(Paint.Style.FILL);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTextView(canvas);
    }

    //private Path[] paths = new Path[3];
    private Path mPath;
    private void drawTextView(Canvas canvas) {

        if(mPath==null){
            mPath = new Path();
            RectF rectF = new RectF(0, 0, mRadius*2, mRadius*2);
            mPath.addOval(rectF, Path.Direction.CCW);
        }
        Rect rectText = new Rect();
        mPaintText.getTextBounds(mText, 0, mText.length(), rectText);
        int textWidth = rectText.width();
        int textWidthHalf = textWidth/2;
        double hudu=Math.asin(textWidthHalf/(mRadius * 1.0f));
        double angle = hudu*180/Math.PI;
        Log.d("ArcTextView","-->angle:"+angle+"--hudu:"+hudu);
        canvas.rotate((float) (90 + angle), getWidth() / 2, getHeight() / 2);
        canvas.drawTextOnPath(mText, mPath, 0, -10, mPaintText);
        //canvas.drawTextOnPath(mText, paths[2], -30, 20, mPaintText);
    }


}

