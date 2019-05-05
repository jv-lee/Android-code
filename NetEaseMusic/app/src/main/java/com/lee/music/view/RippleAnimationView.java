package com.lee.music.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.lee.music.R;
import com.lee.music.ui.UIUtils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 水波纹动画view
 *
 * @author jv.lee
 * @date 2019/5/2
 */
public class RippleAnimationView extends RelativeLayout {

    private Paint mPaint;
    private int rippleColor;
    private int radius;
    private int strokeWidth;
    private ArrayList<RippleCircleView> views = new ArrayList<>();
    private AnimatorSet animatorSet;
    private boolean animationRunning;

    public RippleAnimationView(Context context) {
        this(context, null);
    }

    public RippleAnimationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public boolean isAnimationRunning() {
        return animationRunning;
    }

    private void init(Context context, AttributeSet attris) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        TypedArray typedArray = context.obtainStyledAttributes(attris, R.styleable.RippleAnimationView);
        int rippleType = typedArray.getInt(R.styleable.RippleAnimationView_ripple_anim_type, 0);
        radius = typedArray.getInteger(R.styleable.RippleAnimationView_radius, 54);
        strokeWidth = typedArray.getInteger(R.styleable.RippleAnimationView_strokeWidth, 2);
        rippleColor = typedArray.getColor(R.styleable.RippleAnimationView_ripple_anim_color, ContextCompat.getColor(context, R.color.ripple_color));
        typedArray.recycle();

        if (rippleType == 0) {
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        mPaint.setColor(rippleColor);
        mPaint.setStrokeWidth(UIUtils.getInstance().getWidth(strokeWidth));

        //实例化波纹view
        LayoutParams layoutParams = new LayoutParams(UIUtils.getInstance().getWidth(radius + strokeWidth), UIUtils.getInstance().getWidth(radius + strokeWidth));
        layoutParams.addRule(CENTER_IN_PARENT, TRUE);
        //缩放最大值
        float maxScale = UIUtils.getInstance().displayMetricsWidth / (float) ( (UIUtils.getInstance().getWidth(radius + strokeWidth)));
        //延迟时间
        int rippleDuration = 2500;
        //上一个波纹到下一个波纹间隔时间
        int singleDelay = rippleDuration / 4;
        //动画集合
        ArrayList<Animator> animators = new ArrayList<>();

        //添加水波纹view
        for (int i = 0; i < 4; i++) {
            RippleCircleView rippleCircleView = new RippleCircleView(this);
            addView(rippleCircleView, layoutParams);
            views.add(rippleCircleView);

            //X方向缩放动画
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleCircleView, View.SCALE_X, 1.0F, maxScale);
            scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleXAnimator.setStartDelay(i * singleDelay);
            scaleXAnimator.setDuration(rippleDuration);
            animators.add(scaleXAnimator);

            //Y方向缩放动画
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleCircleView, View.SCALE_Y, 1.0F, maxScale);
            scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleYAnimator.setStartDelay(i * singleDelay);
            scaleYAnimator.setDuration(rippleDuration);
            animators.add(scaleYAnimator);

            //渐变
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(rippleCircleView, View.ALPHA, 1.0F, 0);
            alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
            alphaAnimator.setStartDelay(i * singleDelay);
            alphaAnimator.setDuration(rippleDuration);
            animators.add(alphaAnimator);
        }
        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(animators);

    }

    /**
     * 启动动画
     */
    public void startRippleAnimation(){
        if (!animationRunning) {
            //倒叙
            Collections.reverse(views);
            for (RippleCircleView view : views) {
                view.setVisibility(VISIBLE);
            }
            animatorSet.start();
            animationRunning = true;
        }
    }

    /**
     * 停止动画
     */
    public void stopRippleAnimation(){
        if (animationRunning) {
            Collections.reverse(views);
            for (RippleCircleView view : views) {
                view.setVisibility(INVISIBLE);
            }
            animatorSet.end();
            animationRunning = false;
        }
    }

}
