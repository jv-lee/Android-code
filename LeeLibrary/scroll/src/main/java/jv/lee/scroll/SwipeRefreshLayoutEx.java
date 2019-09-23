package jv.lee.scroll;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * @author jv.lee
 * @date 2019/9/23.
 * @description
 */
public class SwipeRefreshLayoutEx extends SwipeRefreshLayout {

    private float startY;

    private float startX;

    /**
     * 记录HorizontalView是否拖拽的标记
     */
    private boolean isHorizontalTag;

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
