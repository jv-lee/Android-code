package com.lee.library.listener;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.lee.library.base.LeeViewAdapter;

/**
 * @author jv.lee
 * @date 2019/3/29
 */
public interface LeeViewCreate<T> {

    /**
     * 创建SwipeRefresh 下拉刷新
     */
    SwipeRefreshLayout createSwipeRefresh();

    /**
     * SwipeRefresh 下拉颜色
     */
    int[] colorRes();

    /**
     * 创建RecyclerView
     * @return view
     */
    RecyclerView createRecyclerView();

    /**
     * 创建RecyclerView.Adapter
     * @return adapter
     */
    LeeViewAdapter<T> createRecycleViewAdapter();

    /**
     * 创建布局管理器
     * @return 布局管理器
     */
    RecyclerView.LayoutManager createLayoutManager();

    /**
     * 开始页码
     * @return 页码
     */
    int startPageNumber();

    /**
     * 是否支持分页
     * @return boolean
     */
    boolean isSupportPaging();
}
