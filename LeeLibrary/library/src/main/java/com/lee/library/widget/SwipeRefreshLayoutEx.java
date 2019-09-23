package com.lee.library.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * @author jv.lee
 * @date 2019/9/23.
 * @description 处理banner 横向滑动冲突
 */
public class SwipeRefreshLayoutEx extends SwipeRefreshLayout {

    private float startY;

    private float startX;

    /**
     * 记录HorizontalView是否拖拽的标记
     */
    private boolean isHorizontalTag;

    /**
     * 默认触摸滑动生效偏移量
     */
    private final int mTouchSlop;

    public SwipeRefreshLayoutEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
                isHorizontalTag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果HorizontalView 正在拖拽中，那么不拦截它的事件，直接return false；
                if (isHorizontalTag) {
                    return false;
                }
                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 如果X轴位移大于Y轴位移，那么将事件交给HorizontalView处理。
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    //设置当前为横向滑动状态，后续事件交给子view处理 进入move方法直接交给子view处理
                    isHorizontalTag = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                isHorizontalTag = false;
                break;
            default:

        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return super.onInterceptTouchEvent(ev);
    }

}
