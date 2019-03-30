package com.lee.library.listener;

import android.view.View;

/**
 * @author jv.lee
 * @date 2019/3/29
 * item事件监听接口
 */
public interface ItemListener<T> {

    /**
     * item点击时间监听
     * @param view 点击的view
     * @param entity 数据源
     * @param position 下标
     */
    void onItemClick(View view, T entity, int position);

    /**
     * item长按事件监听
     * @param view 长按的view
     * @param entity 数据源
     * @param position 下标
     * @return 是否拦截
     */
    boolean onItemLongClick(View view, T entity, int position);
}
