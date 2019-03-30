package com.lee.library.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


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


    /** 条目点击监听接口 */
    public interface OnItemClickListener<T> {

        /** 点击事件监听 */
        void onItemClick(View view, T entity, int position);
    }

    /** 条目长按监听接口 */
    public interface OnItemLongClickListener<T> {

        /** 长按事件监听 */
        boolean onItemLongClick(View view, T entity, int position);
    }

    /** 设置点击监听属性 */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        adapter.setmOnItemClickListener(onItemClickListener);
    }

    /** 设置长按监听属性 */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        adapter.setmOnItemLongClickListener(onItemLongClickListener);
    }

}
