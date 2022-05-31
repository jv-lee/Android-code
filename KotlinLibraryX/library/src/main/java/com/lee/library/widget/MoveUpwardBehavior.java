package com.lee.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.snackbar.Snackbar;

/**
 *
 * @author jv.lee
 * @date 2019/8/7.
 */
public class MoveUpwardBehavior extends CoordinatorLayout.Behavior<View> {

    public MoveUpwardBehavior() {
        super();
    }

    public MoveUpwardBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    /**
     * 闪避
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY = Math.min(0, ViewCompat.getTranslationY(dependency) - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }

    /**
     * 实现滑动清除消息
     *
     * @param parent
     * @param child
     * @param dependency
     */
    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, View child, View dependency) {
        ViewCompat.animate(child).translationY(0).start();
    }
}
