package com.lee.library.widget.linear

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author jv.lee
 * @date 2020/9/7
 * @description
 */
open class LinearAdapter<T>(val context: Context, val data: ArrayList<T>) {
    private var rootView: ViewGroup? = null
    private val viewItemManager by lazy { LinearViewItemManager<T>() }
    private var viewListener: ItemViewListener<T>? = null
    private val headerViews by lazy { ArrayList<View>() }

    /**
     * 添加多类型样式方法
     *
     * @param item 样式类型
     */
    protected fun addItemStyles(item: LinearViewItem<T>?) {
        viewItemManager.addStyles(item)
    }

    fun bindRoot(view: ViewGroup) {
        this.rootView = view
    }

    fun replaceData(data: ArrayList<T>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun addHeader(view: View) {
        headerViews.add(view)
    }

    fun notifyDataSetChange() {
        rootView?.run {
            removeAllViews()

            for (headerView in headerViews) {
                addView(headerView)
            }

            for ((index, item) in data.withIndex()) {
                val itemViewType = viewItemManager.getItemViewType(item, index)
                val viewItem = viewItemManager.getViewItem(itemViewType)
                val itemView = LayoutInflater.from(context).inflate(viewItem.itemLayout, null, false)
                //渲染数据至itemView
                viewItem.convert(itemView, item, index)
                //设置itemView监听事件
                viewListener?.listener(itemView, item, index)
                addView(itemView)
            }
        }
    }

    fun setItemViewListener(itemViewListener: ItemViewListener<T>) {
        this.viewListener = itemViewListener
    }

    interface ItemViewListener<T> {
        fun listener(itemView: View, entity: T, position: Int)
    }

}