package com.lee.library.ioc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews; // View控件集合
    private View mConvertView; // 当前View

    private RViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    /**
     * 创建RecycleViewViewHold对象
     *
     * @param context 上下文
     * @param parent 父布局
     * @param layoutId 布局ID
     * @return RViewHolder
     */
    static RViewHolder createViewHolder(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new RViewHolder(itemView);
    }

    /**
     * 通过viewId获取控件
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    View getConvertView() {
        return mConvertView;
    }
}
