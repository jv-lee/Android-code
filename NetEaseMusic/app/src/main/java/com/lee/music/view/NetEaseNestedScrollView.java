package com.lee.music.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * @author jv.lee
 * @date 2019/4/30
 * 控制toolbar透明度
 */
public class NetEaseNestedScrollView extends NestedScrollView {

    private ScrollInterface scrollInterface;

    public NetEaseNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public NetEaseNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NetEaseNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 监听滑动 修改toolbar透明度 背景
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (scrollInterface != null){
            scrollInterface.onScrollChange(l,t,oldl,oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public interface ScrollInterface{
        void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }


    public void setScrollInterface(ScrollInterface scrollInterface) {
        this.scrollInterface = scrollInterface;
    }
}
