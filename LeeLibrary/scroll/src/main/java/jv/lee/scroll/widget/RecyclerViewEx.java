package jv.lee.scroll.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author jv.lee
 * @date 2019-09-24
 * @description 子容器
 */
public class RecyclerViewEx extends RecyclerView {

    private int startY = 0;

    public RecyclerViewEx(@NonNull Context context) {
        super(context);
    }

    public RecyclerViewEx(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEx(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int currentY = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //点击后拦截符容器事件
                startY = currentY;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                //获取布局管理器
                LinearLayoutManager layoutManager = null;
                if (getLayoutManager() instanceof LinearLayoutManager) {
                    layoutManager = (LinearLayoutManager) getLayoutManager();
                } else {
                    break;
                }

                //当前子rv 可见item为第一个， 且滑动的y坐标大于最开始点击的y坐标(手指往下滑动 列表向上，已经不再子容器范围内)   打开父容器处理
                if (layoutManager.findFirstVisibleItemPosition() == 0 && currentY > startY) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                    //当前子rv 可见item为最后一个，且滑动的y坐标小于最开始点击的y坐标(手指往上滑动 列表向下)，已经不再子容器范围内  打开父容器处理
                } else if (layoutManager.findLastVisibleItemPosition() == layoutManager.getItemCount() - 1 && currentY < startY) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                }
                //父容器不处理
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
