package com.lee.library.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * @author jv.lee
 */
public class LeeRecyclerView extends RecyclerView {

    private LeeViewAdapter adapter;

    public LeeRecyclerView(@NonNull Context context) {
        super(context);
    }

    public LeeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LeeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setRecyclerAdapter(LeeViewAdapter adapter) {
        this.adapter = adapter;
        setAdapter(adapter);
    }


    /**
     * 条目点击监听接口
     */
    public interface OnItemClickListener<T> {

        /**
         * 点击事件监听
         */
        void onItemClick(View view, T entity, int position);
    }

    /**
     * 返回所有子view
     * @param <T>
     */
    public interface OnItemChildView<T>{
        /**
         * 点击事件监听
         */
        void onItemChild(LeeViewHolder viewHolder, List<T> data);
    }

    /**
     * 条目长按监听接口
     */
    public interface OnItemLongClickListener<T> {

        /**
         * 长按事件监听
         */
        boolean onItemLongClick(View view, T entity, int position);
    }

    /**
     * 自动加载更多监听接口
     */
    public interface AutoLoadMoreListener {
        /**
         * 回调需要加载更多
         */
        void autoLoadMore();
    }

    public void setOnItemChildClickListener(OnItemChildView onItemChildView) {
        adapter.setOnItemChildClickListener(onItemChildView);
    }

    public void setAutoLoadMoreListener(AutoLoadMoreListener autoLoadMoreListener) {
        adapter.setAutoLoadMoreListener(autoLoadMoreListener);
    }

    /**
     * 设置点击监听属性
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        adapter.setOnItemClickListener(onItemClickListener);
    }

    /**
     * 设置长按监听属性
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        adapter.setOnItemLongClickListener(onItemLongClickListener);
    }

}
