package com.lee.library.widget.linear;

import android.view.View;

/**
 * @author jv.lee
 * @date 2020/9/7
 * @description 某单一样式
 */
public interface LinearViewItem<T> {

    /**
     * 获取item的布局
     *
     * @return 布局id
     */
    int getItemLayout();


    /**
     * 是否为当前的item布局
     *
     * @param entity
     * @param position
     * @return
     */
    boolean isItemView(T entity, int position);

    /**
     * 将item的控件与需要显示数据绑定
     *
     * @param itemView
     * @param entity
     * @param position
     */
    void convert(View itemView, T entity, int position);


}
