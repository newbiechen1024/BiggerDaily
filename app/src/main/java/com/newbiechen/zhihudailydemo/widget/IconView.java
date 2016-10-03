package com.newbiechen.zhihudailydemo.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.newbiechen.androidlib.utils.MetricsUtils;
import com.newbiechen.zhihudailydemo.R;

import java.util.Currency;

/**
 * Created by PC on 2016/10/2.
 * 作用：知乎Splash界面上，展示logo的动态图标
 *
 * 1、首先制作出边框（drawRoundRect桌）
 * 2、制作出半圆
 * 3、半圆的动画 + 动画结束的回调接口
 *
 * 4、做好measure适配 ，无视padding
 * 5、xml中边框和半圆的颜色选择(统一颜色吧)。 圆的大小默认为长度的1/3吧
 *   暂时不支持在代码值变换
 */
public class IconView extends View{

    private static final String TAG = "IconView";
    //默认为正方形，宽度为50dp
    private static final int DEFAULT_SIZE = 50;
    //默认为白色。
    private static final int DEFAULT_COLOR = 0xffffffff;
    //默认边框的尺寸
    private static final int DEFAULT_FRAME_STROKE = 2;
    //默认扇形的尺寸
    private static final int DEFAULT_ARC_STROKE = 5;
    //扇形的初始角度
    private static final int ORIGIN_ARC_ANGLE = 90;
    //扇形扫过的角度
    private static final int SWEEP_ARC_ANGLE = 270;

    private final Paint mFramePaint = new Paint();
    private final Paint mArcPaint = new Paint();
    private final Path mArcPath = new Path();
    private final ValueAnimator mArcAnim = new ValueAnimator();

    private OnIconAnimFinishListener mFinishListener;

    //View的大小
    private int mViewWidth;
    private int mViewHeight;

    //可以通过xml来替换的配置
    private int mColorStyle = DEFAULT_COLOR;
    private float mFrameStroke = MetricsUtils.dp2px(DEFAULT_FRAME_STROKE);
    private float mArcStroke = MetricsUtils.dp2px(DEFAULT_ARC_STROKE);

    //半圆当前扫过的距离
    private float mSweepAngle = 0;


    public IconView(Context context) {
        this(context,null);
    }

    public IconView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(attrs);
        initWidget();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    private void initAttribute(AttributeSet attrs){
       if (attrs != null){
           TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.IconView);
           mColorStyle = a.getColor(R.styleable.IconView_color_style,mColorStyle);
           mFrameStroke = a.getDimension(R.styleable.IconView_frame_stroke,mFrameStroke);
           mArcStroke = a.getDimension(R.styleable.IconView_arc_stroke,mArcStroke);
       }
    }

    private void initWidget(){
        //初始化边框的画笔
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setStrokeWidth(mFrameStroke);
        mFramePaint.setColor(mColorStyle);
        mFramePaint.setAntiAlias(true);
        mFramePaint.setDither(true);
        //初始化半圆的画笔
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcStroke);
        mArcPaint.setColor(mColorStyle);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);
        //扇形的边角为圆形
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        Log.d(TAG,mArcStroke+"什么鬼");
        //半圆的动画
        setUpArcAnim();
    }

    private void setUpArcAnim(){
        mArcAnim.setFloatValues(SWEEP_ARC_ANGLE);
        mArcAnim.setInterpolator(new LinearInterpolator());
        mArcAnim.setDuration(1000);
        mArcAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentSweepAngle = (float)animation.getAnimatedValue();
                mSweepAngle = currentSweepAngle;
                //重绘
                invalidate();
                //动画完成的回调
                if (currentSweepAngle == SWEEP_ARC_ANGLE && mFinishListener != null){
                    mFinishListener.onAnimFinish();
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measureWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        float defaultSize = MetricsUtils.dp2px(DEFAULT_SIZE);
        float finishWidth = 0;
        float finishHeight = 0;

        if (measureWidthMode == MeasureSpec.AT_MOST &&
                measureHeightMode == MeasureSpec.AT_MOST){
            finishWidth = defaultSize;
            finishHeight = defaultSize;
        }
        else if (measureWidthMode == MeasureSpec.AT_MOST &&
                    measureHeightMode == MeasureSpec.EXACTLY){
            finishWidth = defaultSize;
            finishHeight = measureHeightSize;
        }
        else if (measureHeightMode == MeasureSpec.AT_MOST &&
                    measureWidthMode == MeasureSpec.EXACTLY){
            finishWidth = measureWidthSize;
            finishHeight = defaultSize;
        }
        else {
            finishWidth = measureWidthSize;
            finishHeight = measureHeightSize;
        }
        setMeasuredDimension((int) finishWidth,(int) finishHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawViewFrame(canvas);
        drawViewArc(canvas);
    }

    private void drawViewFrame(Canvas canvas){
        RectF frameRectF = new RectF(0,0,mViewWidth,mViewHeight);
        canvas.drawRoundRect(frameRectF,30,30,mFramePaint);
    }

    private void drawViewArc(Canvas canvas){
        canvas.save();
        mArcPath.reset();

        //将画布移到View的中心
        canvas.translate(mViewWidth/2,mViewHeight/2);

        //间距为1/3
        float radius = (mViewWidth*2/3 - mArcStroke)/2;
        RectF mArcRectF = new RectF(-radius,-radius,radius,radius);
        mArcPath.addArc(mArcRectF,ORIGIN_ARC_ANGLE,mSweepAngle);

        //绘制
        canvas.drawPath(mArcPath,mArcPaint);
        canvas.restore();
    }

    /**
     * 当动画结束的时候，的回调
     */
    public interface OnIconAnimFinishListener{
        void onAnimFinish();
    }

    /************************公共的方法***********************************/
    /**
     * 开启动画
     */
    public void startAnimation(){
        if (!mArcAnim.isRunning()){
            mArcAnim.start();
        }
    }

    /**
     * 设置动画结束的回调
     * @param listener 回调的监听器
     */
    public void setOnIconAnimFinishListener(OnIconAnimFinishListener listener){
        mFinishListener = listener;
    }
}
