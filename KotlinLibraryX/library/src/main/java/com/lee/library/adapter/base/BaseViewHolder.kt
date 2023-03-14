package com.lee.library.adapter.base

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 封装的RecyclerViewHolder
 *
 * @author jv.lee
 * @date 2019/3/29
 */
open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mViews = SparseArray<View>()

    /**
     * @return 返回当前itemView
     */
    fun getConvertView() = itemView

    /**
     * 通过R.id.xxx获取的某个控件
     *
     * @param viewId 控件ID
     * @param <T>    业务类型
     * @return 返回通过资源id获取的控件
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(viewId: Int): T {
        var view = mViews[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }

    companion object {
        fun createViewHolder(context: Context, parent: ViewGroup, layoutId: Int): BaseViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutId, parent, false)
            return BaseViewHolder(itemView)
        }
    }

}