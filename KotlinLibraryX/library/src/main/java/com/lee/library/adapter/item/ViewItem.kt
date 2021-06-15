package com.lee.library.adapter.item

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.lee.library.adapter.base.BaseViewHolder
import com.lee.library.adapter.base.BaseViewItem
import com.lee.library.adapter.binding.ViewBindingHolder

/**
 * @author jv.lee
 * @date 2021/6/15
 * @description
 */
abstract class ViewItem<T> :
    BaseViewItem<T> {

    override fun getItemViewAny(context: Context, parent: ViewGroup): Any {
        return getItemView(context, parent)
    }

    override fun viewRecycled(holder: BaseViewHolder, entity: T, position: Int) {
        viewRecycled(holder,entity, position)
    }

    abstract fun getItemView(context: Context, parent: ViewGroup): View


    fun viewRecycled(holder: ViewBindingHolder, entity: T, position: Int) {}

}