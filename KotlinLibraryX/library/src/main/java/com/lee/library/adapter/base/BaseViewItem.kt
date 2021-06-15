package com.lee.library.adapter.base

import android.content.Context
import android.view.ViewGroup

/**
 * @author jv.lee
 * @date 2019/3/29
 * 某一类的item对象
 */
interface BaseViewItem<T> {
    /**
     * 获取itemView 的实例（view、viewBinding)
     *
     */
    fun getItemViewAny(context: Context, parent: ViewGroup): Any

    /**
     * 是否开启item点击
     *
     * @return boolean值
     */
    fun openClick(): Boolean = true

    /**
     * 是否打开防抖
     *
     * @return boolean
     */
    fun openShake(): Boolean = true

    /**
     * 是否打开回收回调
     * @return boolean
     */
    fun openRecycler(): Boolean = false

    /**
     * 是否为当前的item布局
     *
     * @param entity
     * @param position
     * @return
     */
    fun isItemView(entity: T, position: Int): Boolean = true

    /**
     * 将item的控件与需要显示数据绑定
     *
     * @param holder
     * @param entity
     * @param position
     */
    fun convert(holder: BaseViewHolder, entity: T, position: Int)

    /**
     * 当item隐藏后 回调销毁及回收操作
     * @param holder
     * @param entity
     * @param position
     */
    fun viewRecycled(holder: BaseViewHolder, entity: T, position: Int) {}
}