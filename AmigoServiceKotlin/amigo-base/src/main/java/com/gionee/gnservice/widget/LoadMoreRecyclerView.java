package com.gionee.gnservice.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.gionee.gnservice.utils.LogUtil;

/**
 * Created by caocong on 6/8/17.
 */
public class LoadMoreRecyclerView extends RecyclerView {
    private static final String TAG = LoadMoreRecyclerView.class.getSimpleName();

    private CustomOnScrollListener mOnScrollListener;

    public LoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public abstract static class CustomOnScrollListener extends OnScrollListener {
        private LayoutManagerType mLayoutManagerType;
        private int mLastVisibleItemPosition;
        private int[] mLastVisibleItemPositionStaggered;
        private int mCurrentScrollState;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (mLayoutManagerType == null) {
                if (layoutManager instanceof GridLayoutManager) {
                    mLayoutManagerType = LayoutManagerType.GRID;
                } else if (layoutManager instanceof LinearLayoutManager) {
                    mLayoutManagerType = LayoutManagerType.LINEAR;
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    mLayoutManagerType = LayoutManagerType.STAGGERED_GRID;
                } else {
                    throw new RuntimeException("not support");
                }
            }
            switch (mLayoutManagerType) {
                case LINEAR:
                    mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case GRID:
                    mLastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case STAGGERED_GRID:
                    StaggeredGridLayoutManager sg = (StaggeredGridLayoutManager) layoutManager;
                    if (mLastVisibleItemPositionStaggered == null) {
                        mLastVisibleItemPositionStaggered = new int[sg.getSpanCount()];
                    }
                    sg.findLastVisibleItemPositions(mLastVisibleItemPositionStaggered);
                    mLastVisibleItemPosition = findMax(mLastVisibleItemPositionStaggered);
                    break;
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            mCurrentScrollState = newState;
            int visiableItemCount = recyclerView.getLayoutManager().getChildCount();
            int totalItemCount = recyclerView.getLayoutManager().getItemCount();
            if (visiableItemCount > 0 && mCurrentScrollState == RecyclerView.SCROLL_STATE_IDLE
                    && mLastVisibleItemPosition >= totalItemCount - 1) {
                onLoadMore(recyclerView);
            }
        }

        public abstract void onLoadMore(RecyclerView recyclerView);

        private int findMax(int[] lastPositions) {
            int max = Integer.MIN_VALUE;
            for (int value : lastPositions) {
                LogUtil.d(TAG, "value:" + value + " max:" + max);
                if (value > max)
                    max = value;
            }
            return max;
        }
    }

    private enum LayoutManagerType {
        LINEAR,
        GRID,
        STAGGERED_GRID,
    }


}
