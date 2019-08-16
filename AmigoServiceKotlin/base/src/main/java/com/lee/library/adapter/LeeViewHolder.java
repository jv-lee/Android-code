package com.lee.library.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author jv.lee
 * @date 2019/3/29
 * 封装的RecyclerViewHolder
 */
public class LeeViewHolder extends RecyclerView.ViewHolder {

    /**
     * 所有的控件的集合
     */
    private SparseArray<View> mViews;
    /**
     * 当前View对象
     */
    private View mConvertView;

    private LeeViewHolder(@NonNull View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    /**
     * 创建RViewHolder对象
     * 创建RViewHolder对象
     * @param context 上下文对象
     * @param parent 父容器
     * @param layoutId 布局id
     * @return 返回ViewHolder
     */
    public static LeeViewHolder createViewHolder(Context context, ViewGroup parent, int layoutId){
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new LeeViewHolder(itemView);
    }

    /**
     * 获取当前View
     * @return 返回当前itemView
     */
    public View getConvertView(){
        return mConvertView;
    }

    /**
     * 通过R.id.xxx获取的某个控件
     * @param viewId 控件ID
     * @param <T> 业务类型
     * @return 返回通过资源id获取的控件
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }

}
