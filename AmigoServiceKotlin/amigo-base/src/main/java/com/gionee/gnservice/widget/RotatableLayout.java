package com.gionee.gnservice.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class RotatableLayout extends FrameLayout {
    private static final int DEFAULT_ROTATE_DURATION = 600;
    private View mFrontView;
    private View mBackView;
    private boolean mIsFrontViewVisible = true;
    private boolean mRotating = false;
    private OnRotateListener mOnRotateListener;

    private static final float FRONT_TO_BACK = -180;
    private static final float BACK_TO_FRONT = 0;

    public RotatableLayout(Context context) {
        this(context, null);
    }

    public RotatableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RotatableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCameraDistance();
    }

    private void setCameraDistance() {
        int distance = 10000;
        float scale = getResources().getDisplayMetrics().density * distance;
        setCameraDistance(scale);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() < 2) {
            throw new IllegalArgumentException("child view count is not vaild");
        }
        mBackView = getChildAt(0);
        mFrontView = getChildAt(1);
    }

    public void startRotate() {
        rotate(mIsFrontViewVisible ? FRONT_TO_BACK : BACK_TO_FRONT);
    }

    private void rotate(float degree) {
        if (mRotating) {
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new LinearInterpolator());
        ArrayList<Animator> animators = new ArrayList<>();

        Animator initBackViewAnimator = ObjectAnimator.ofFloat(mBackView, View.ROTATION_Y, FRONT_TO_BACK).setDuration(0);
        animators.add(initBackViewAnimator);

        animators.add(getRotateAnimator(degree));

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRotating = false;
                super.onAnimationEnd(animation);
                if (mOnRotateListener != null) {
                    mOnRotateListener.onRotated(mIsFrontViewVisible);
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mRotating = true;
                super.onAnimationStart(animation);

            }
        });
        animatorSet.playSequentially(animators);
        animatorSet.start();
    }

    private Animator getRotateAnimator(float degree) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, View.ROTATION_Y, degree).setDuration(DEFAULT_ROTATE_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                swapViews();
            }
        });

        return animator;
    }

    private boolean isInFrontArea() {
        float currentYRotation = getRotationY();
        return (-270 >= currentYRotation && currentYRotation >= -360)
                || (-90 <= currentYRotation && currentYRotation <= 90)
                || (270 <= currentYRotation && currentYRotation <= 360);
    }

    private void swapViews() {
        boolean isFront = isInFrontArea();
        if (isFront && !mIsFrontViewVisible) {
            mBackView.setVisibility(View.INVISIBLE);
            mFrontView.setVisibility(View.VISIBLE);
            mIsFrontViewVisible = true;
            return;
        }
        if (!isFront && mIsFrontViewVisible) {
            mBackView.setVisibility(View.VISIBLE);
            mFrontView.setVisibility(View.INVISIBLE);
            mIsFrontViewVisible = false;
        }
    }

    public interface OnRotateListener {
        void onRotated(boolean isFront);
    }

    public void setOnRotateListener(OnRotateListener listener) {
        mOnRotateListener = listener;
    }

}