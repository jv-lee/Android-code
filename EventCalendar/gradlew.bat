package com.dreame.reader.common.ui.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author: 陈勇
 * Version: 1.0.0
 * Date: 2017/1/14
 * Mender:
 * Modify:
 * Description: 自定义ScrollView，解决不能设置滑动监听的问题
 */
public class ScrollView extends android.widget.ScrollView {

    private static final long SEND_MESSAGE_DELAYED = 5;

    /**
     * 滑动回调接口
     */
    private OnScrollListener mOnScrollListener = null;
    /**
     * 记录Y轴方向上最后滑动的距离，当手指离开ScrollView时，ScrollView还在继续滑动，
     * 通过mHandler.sendEmptyMessageDelayed()的方式继续回调滑动监听，在handleMessage中
     * 判断当滑动的距离等于mLastScrollY时则停止sendEmptyMessageDelayed()
     */
    private int mLastScrollY = 0;
    /**
     * getScrollY()不变记数，处理快速滑动时getScrollY()“不准确”的问题，
     * 快速滑动时会出现getScrollY()和实际值不相等的“不准确”的问题，再次getScrollY()时则“准确”，
     * 当getScrollY()不变时再多调用几次getScrollY()
     */
    private int mScrollYUnchangedCount = 0;

    public ScrollView(Context context) {
        this(context, null);
    }

    public ScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mLastScrollY = getScrollY();

        if (mOnScrollListener != null) {
            mOnScrollListener.onScrolled(mLastScrollY);
        }

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mHandler.sendEmptyMessageDelayed(0, SEND_MESSAGE_DELAYED);
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 设置滑动回调
     *
     * @param listener
     */
    public void setOnScrollListener(OnScrollListener listener) {
        mOnScrollListener = listener;
  