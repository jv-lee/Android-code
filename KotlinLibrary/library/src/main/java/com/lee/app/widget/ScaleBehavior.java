package com.lee.app.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author jv.lee
 * @date 2019/4/5
 */
public class ScaleBehavior<V extends View>  extends CoordinatorLayout.Behavior<V> {

    private FastOutLinearInInterpolator linearInInterpolator = new FastOutLinearInInterpolator();
    private LinearOutSlowInInterpolator linearOutSlowInInterpolator = new LinearOutSlowInInterpolator();
    private boolean isRunning;

    public ScaleBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        //垂直滚动
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        //条件判断 view是显示的 且 动画不在执行
        //向下滑动 缩放隐藏
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE && !isRunning) {
            scaleHide(child);

            //向上滑动 缩放显示控件
        } else if (dyConsumed < 0 && child.getVisibility() == View.INVISIBLE && !isRunning) {
            scaleShow(child);
        }
    }

    private void scaleShow(V child) {
        child.setVisibility(View.VISIBLE);
        ViewCompat.animate(child)
                .scaleX(1)
                .scaleY(1)
                .setDuration(500)
                .setInterpolator(linearOutSlowInInterpolator)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        isRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        isRunning = false;
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        isRunning = false;
                    }
                });
    }

    /**
     * 属性动画 设置缩放到隐藏
     * @param child
     */
    private void scaleHide(V child) {
        ViewCompat.animate(child)
                .scaleX(0)
                .scaleY(0)
                .setDuration(500)
                .setInterpolator(linearInInterpolator)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        isRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        isRunning = false;
                        child.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        isRunning = false;
                    }
                });
    }
}
