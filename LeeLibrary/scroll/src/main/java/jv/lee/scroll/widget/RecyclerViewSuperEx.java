package jv.lee.scroll.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author jv.lee
 * @date 2019-09-24
 * @description 父容器
 */
public class RecyclerViewSuperEx extends RecyclerView {

    public RecyclerViewSuperEx(@NonNull Context context) {
        super(context);
    }

    public RecyclerViewSuperEx(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewSuperEx(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        //父容器按下事件时继续向下分发给子view， 其他事件全部自身处理， 在Down的时候 直接交给子view来分发事件和开关 符容器事件处理机制
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            return super.onInterceptTouchEvent(e);
        }else{
            return true;
        }
    }
}
