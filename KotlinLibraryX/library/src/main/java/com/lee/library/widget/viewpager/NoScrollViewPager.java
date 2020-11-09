package com.lee.library.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * @author jv.lee
 */
public class NoScrollViewPager extends ViewPager {
    private boolean noScroll = false;
    private boolean noSmoothScroll = false;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    public void setNoSmoothScroll(boolean noSmoothScroll) {
        this.noSmoothScroll = noSmoothScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll) {
            return false;
        } else {
            return super.onTouchEvent(arg0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll) {
            return false;
        } else {
            return super.onInterceptTouchEvent(arg0);
        }
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (!noSmoothScroll) {
            super.setCurrentItem(item, smoothScroll);
        }else{
            super.setCurrentItem(item, true);
        }

    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }
}