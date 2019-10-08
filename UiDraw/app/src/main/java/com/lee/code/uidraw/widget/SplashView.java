package com.lee.code.uidraw.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.lee.code.uidraw.R;

/**
 * @author jv.lee
 * @description 扩散展示蒙层View
 */
public class SplashView extends View {
    /**
     * 旋转圆的画笔
     */
    private Paint mPaint;
    /**
     * 扩散圆的画笔
     */
    private Paint mHolePaint;
    /**
     * 属性动画
     */
    private ValueAnimator mValueAnimator;

    /**
     * 背景色
     */
    private int mBackgroundColor = Color.WHITE;
    private int[] mCircleColors;

    /**
     * 表示旋转圆的中心坐标
     */
    private float mCenterX;
    private float mCenterY;

    /**
     * 表示倾斜对角线长度的一半，扩散圆最大半径 水波纹动画扩散的最大半径
     */
    private float mDistance;

    /**
     * 6个小球的半径
     */
    private float mCircleRadius = 18;

    /**
     * 旋转大圆的半径
     */
    private float mRotateRadius = 90;

    /**
     * 当前大圆的旋转角度
     */
    private float mCurrentRotateAngle = 0f;

    /**
     * 当前大圆的半径
     */
    private float mCurrentRotateRadius = mRotateRadius;

    /**
     * 扩散圆的半径
     */
    private float mCurrentHoleRadius = 0f;

    /**
     * 表示旋转动画的时长
     */
    private int mRotateDuration = 1200;


    public SplashView(Context context) {
        this(context, null);
    }

    public SplashView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHolePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHolePaint.setStyle(Paint.Style.STROKE);
        mHolePaint.setColor(mBackgroundColor);

        mCircleColors = context.getResources().getIntArray(R.array.splash_circle_colors);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w * 0.5f;
        mCenterY = h * 0.5f;
        //获取宽高斜角线的一半  等于获取屏幕左上角到右下角的一条线的一半距离
        mDistance = (float) (Math.hypot(w, h) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState == null) {
            mState = new RotateState();
        }
        mState.drawState(canvas);
    }

    private SplashState mState;

    private abstract class SplashState {
        abstract void drawState(Canvas canvas);
    }

    /**
     * 1.旋转动画
     */
    private class RotateState extends SplashState {

        private RotateState() {
            //旋转一周
            mValueAnimator = ValueAnimator.ofFloat(0, (float) (Math.PI * 2));
            //执行两遍
            mValueAnimator.setRepeatCount(2);
            mValueAnimator.setDuration(mRotateDuration);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotateAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            //监听动画结束
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    //切换扩散聚合动画
                    mState = new MerginState();
                }
            });
            mValueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            //绘制背景
            drawBackground(canvas);
            //绘制6个小球
            drawCircles(canvas);
        }
    }

    /**
     * 绘制6个小球
     *
     * @param canvas
     */
    private void drawCircles(Canvas canvas) {
        //获取6个球的角度
        float rotateAngle = (float) (Math.PI * 2 / mCircleColors.length);
        for (int i = 0; i < mCircleColors.length; i++) {
            // x = r * cos(a) + centX;
            // y = r * sin(a) + centY;
            //获得动画旋转的角度相加 才能重新绘制
            float angle = i * rotateAngle + mCurrentRotateAngle;
            float cx = (float) (Math.cos(angle) * mCurrentRotateRadius + mCenterX);
            float cy = (float) (Math.sin(angle) * mCurrentRotateRadius + mCenterY);
            mPaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
        }
    }


    /**
     * 2.扩散聚合动画
     */
    private class MerginState extends SplashState {

        private MerginState() {
            //从小圆到大圆的距离
            mValueAnimator = ValueAnimator.ofFloat(mCircleRadius, mRotateRadius);
            mValueAnimator.setDuration(mRotateDuration);
            //添加线性插值器 此插值器会往外扩散再执行效果
            mValueAnimator.setInterpolator(new OvershootInterpolator(10f));
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotateRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            //监听动画结束
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    //切换扩散聚合动画
                    mState = new ExpandState();
                }
            });
            mValueAnimator.reverse();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
            drawCircles(canvas);
        }
    }


    /**
     * 3.水波纹动画
     */
    private class ExpandState extends SplashState {

        public ExpandState() {
            //从小圆到水波纹整个屏幕大圆的距离
            mValueAnimator = ValueAnimator.ofFloat(mCircleRadius, mDistance);
            mValueAnimator.setDuration(mRotateDuration);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentHoleRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
        }
    }

    /**
     * 绘制背景
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        if (mCurrentHoleRadius > 0) {
            //绘制空心圆 因为是最后一个动画
            float strokeWidth = mDistance - mCurrentHoleRadius;
            float radius = strokeWidth / 2 + mCurrentHoleRadius;
            mHolePaint.setStrokeWidth(strokeWidth);
            canvas.drawCircle(mCenterX, mCenterY, radius, mHolePaint);
        } else {
            canvas.drawColor(mBackgroundColor);

        }
    }

}
