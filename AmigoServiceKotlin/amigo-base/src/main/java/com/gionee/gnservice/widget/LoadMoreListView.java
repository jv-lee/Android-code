package com.gionee.gnservice.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.gionee.gnservice.utils.ResourceUtil;

/**
 * Created by matou0289 on 2016/10/14.
 */

public class LoadMoreListView extends ListView {
    private View mLoadMoreFooter;
    private OnLoadMoreListener mLoadMoreListener;
    private boolean mAddedFooter;
    private boolean mLoadMore;

    public interface OnLoadMoreListener extends OnScrollListener {
        void onLoadMoreData();
    }

    public LoadMoreListView(Context context) {
        super(context);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLoadMoreFooter = LayoutInflater.from(getContext()).inflate(ResourceUtil.getLayoutId(getContext(), "uc_listview_load_more"), null);
        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                    // 滑动到底部显示LoadingMore
                    if (firstVisibleItem + visibleItemCount == totalItemCount && mLoadMore) {
                        showLoadMoreView();
                        // 加载耗时操作
                        post(new Runnable() {
                            @Override
                            public void run() {
                                mLoadMoreListener.onLoadMoreData();
                                mLoadMore = false;
                            }
                        });
                    }
                }
            }
        });
    }

    // 加载数据完成
    public void loadMoreComplete() {
        deferNotifyDataSetChanged();
        setLoadMore(false);
    }

    public void setLoadMore(boolean loadMore) {
        mLoadMore = loadMore;
        if (!loadMore) {
            removeFooterView(mLoadMoreFooter);
        }
    }


    private void showLoadMoreView() {
        if (!mAddedFooter) {
            addFooterView(mLoadMoreFooter);
            mAddedFooter = true;
        }
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
    }
}
