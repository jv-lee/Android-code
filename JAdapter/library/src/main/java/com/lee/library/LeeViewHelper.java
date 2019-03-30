package com.lee.library;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import com.lee.library.base.LeeViewAdapter;
import com.lee.library.listener.LeeViewCreate;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019/3/29
 */
public class LeeViewHelper<T> {

    private SwipeRefreshLayout swipeRefresh;
    private SwipeRefreshHelper swipeRefreshHelper;
    private RecyclerView recyclerView;
    private LeeViewAdapter<T>adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private int startPageNumber;
    private boolean isSupportPaging;
    private SwipeRefreshHelper.SwipeRefreshListener listener;
    private int currentPageNumber;

    private LeeViewHelper(Builder<T> builder) {
        this.swipeRefresh = builder.create.createSwipeRefresh();
        this.recyclerView = builder.create.createRecyclerView();
        this.adapter = builder.create.createRecycleViewAdapter();
        this.layoutManager = builder.create.createLayoutManager();
//        this.itemDecoration = builder.create.create
        this.startPageNumber = builder.create.startPageNumber();
        this.isSupportPaging = builder.create.isSupportPaging();
        this.listener = builder.listener;

        this.currentPageNumber = this.startPageNumber;
        int[] colorRes = builder.create.colorRes();
        if (swipeRefresh != null) {
            if (colorRes == null) {
                swipeRefreshHelper = SwipeRefreshHelper.createSwipeRefreshHelper(swipeRefresh);
            }else{
                swipeRefreshHelper = SwipeRefreshHelper.createSwipeRefreshHelper(swipeRefresh, colorRes);
            }
        }
        init();
    }

    private void init() {
        //RecyclerView初始化
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (itemDecoration != null) {
            recyclerView.addItemDecoration(itemDecoration);
        }

        //下拉刷新
        if (swipeRefreshHelper != null) {
            swipeRefreshHelper.setSwiperefreshListener(() -> {
                //下拉刷新是否 == 重新加载
                currentPageNumber = startPageNumber;
                dismissSwipeRefresh();
                if (listener != null) {
                    listener.onRefresh();
                }
            });
        }
    }

    /**
     * 关闭刷新
     */
    private void dismissSwipeRefresh() {
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    public void notifyAdapterDataSetChanged(List datas) {
        //首次加载或者下啦刷新后都要重置页码
        if (currentPageNumber == startPageNumber) {
            adapter.updateDatas(datas);
        }else{
            adapter.addDatas(datas);
        }
        recyclerView.setAdapter(adapter);
        //省略功能 加载更多、最后一条、空布局
        if (isSupportPaging) {

        }
    }

    public static class Builder<T>{
        /**
         * 初始化接口
         */
        private LeeViewCreate<T> create;
        /**
         * 下拉刷新
         */
        private SwipeRefreshHelper.SwipeRefreshListener listener;

        public Builder(LeeViewCreate<T> create, SwipeRefreshHelper.SwipeRefreshListener listener) {
            this.create = create;
            this.listener = listener;
        }

        public LeeViewHelper build() {
            //参数校验
            return new LeeViewHelper<>(this);
        }
    }

}
