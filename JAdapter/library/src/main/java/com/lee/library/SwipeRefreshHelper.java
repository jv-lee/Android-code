package com.lee.library;

import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * @author jv.lee
 * @date 2019/3/29
 * 下拉刷新的帮助类
 */
public class SwipeRefreshHelper {

    private SwipeRefreshLayout swipeRefresh;
    private SwipeRefreshListener swiperefreshListener;

    static SwipeRefreshHelper createSwipeRefreshHelper(SwipeRefreshLayout swipeRefresh, @ColorRes int... colorResIds) {
        return new SwipeRefreshHelper(swipeRefresh, colorResIds);
    }

    private SwipeRefreshHelper(@Nullable SwipeRefreshLayout swipeRefresh, @ColorRes int... colorResIds) {
        this.swipeRefresh = swipeRefresh;
        init(colorResIds);
    }

    private void init(int[] colorResIds) {
        if (colorResIds == null || colorResIds.length == 0) {
            swipeRefresh.setColorSchemeResources(android.R.color.holo_orange_dark,
                    android.R.color.holo_green_dark, android.R.color.holo_blue_dark);
        }else{
            swipeRefresh.setColorSchemeResources(colorResIds);
        }
        swipeRefresh.setOnRefreshListener(()->{
            if (swiperefreshListener != null) {
                swiperefreshListener.onRefresh();
            }
        });
    }

    public interface SwipeRefreshListener{
        /**
         * 刷新回调
         */
        void onRefresh();
    }

    public void setSwiperefreshListener(SwipeRefreshListener swiperefreshListener) {
        this.swiperefreshListener = swiperefreshListener;
    }
}
