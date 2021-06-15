package com.lee.api.adapter;

import androidx.viewbinding.ViewBinding;

/**
 * @author jv.lee
 * @date 2019/3/29
 * 某一类的item对象
 */
public interface ViewBindingItem<T> {

    /**
     * 获取item的布局
     *
     * @return 布局id
     */
    ViewBinding bindViewBinding();

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
     * 是否打开回收回调
     * @return boolean
     */
    boolean openRecycler();

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
    void convert(ViewBindingHolder holder, T entity, int position);

    /**
     * 当item隐藏后 回调销毁及回收操作
     * @param holder
     * @param entity
     * @param position
     */
    void viewRecycled(ViewBindingHolder holder, T entity, int position);

}
