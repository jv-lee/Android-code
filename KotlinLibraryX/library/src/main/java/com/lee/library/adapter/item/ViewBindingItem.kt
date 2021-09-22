package com.lee.library.adapter.item

import android.content.Context
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lee.library.adapter.base.BaseViewHolder
import com.lee.library.adapter.base.BaseViewItem
import com.lee.library.adapter.binding.ViewBindingHolder

/**
 * @author jv.lee
 * @date 2021/6/15
 * @description
 */
abstract class ViewBindingItem<T> :
    BaseViewItem<T> {

    override fun getItemViewAny(context: Context, parent: ViewGroup): Any {
        return getItemViewBinding(context, parent)
    }

    override fun convert(holder: BaseViewHolder, entity: T, position: Int) {
        this.convert(holder as ViewBindingHolder, entity, position)
    }

    override fun viewRecycled(holder: BaseViewHolder) {
        this.viewRecycled(holder as ViewBindingHolder)
    }

    abstract fun getItemViewBinding(context: Context, parent: ViewGroup): ViewBinding

    abstract fun convert(holder: ViewBindingHolder, entity: T, position: Int)

    open fun viewRecycled(holder: ViewBindingHolder) {}

}