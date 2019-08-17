package com.lee.app.adapter.listener;

import com.lee.app.adapter.LeeViewHolder;

/**
 * @author jv.lee
 * @date 2019/3/29
 * 某一类的item对象
 */
public interface LeeViewItem<T> {

    /**
     * 获取item的布局
     *
     * @return 布局id
     */
    int getItemLayout();

    /**
     * 是否开启item点击
     *
     * @return boolean值
     */
    boolean openClick();

    /**
     * 是否打开防抖
     *
     * @return boolean
     */
    boolean openShake();

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
     * @param holder
     * @param entity
     * @param position
     */
    void convert(LeeViewHolder holder, T entity, int position);

}
