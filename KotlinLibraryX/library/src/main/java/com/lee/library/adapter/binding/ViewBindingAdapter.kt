package com.lee.library.adapter.binding

import android.content.Context
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lee.library.adapter.base.BaseViewAdapter
import com.lee.library.adapter.base.BaseViewHolder
import com.lee.library.adapter.base.BaseViewItem

/**
 * @author jv.lee
 * @date 2019/3/29
 * [BaseViewAdapter] 的viewBinding实现 ,使用viewBinding解析view时可使用该适配器基类
 */
open class ViewBindingAdapter<T> : BaseViewAdapter<T> {

    constructor(context: Context, data: List<T>) : super(context, data)

    constructor(context: Context, data: List<T>, item: BaseViewItem<T>) : super(context, data, item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        //根据布局的类型 创建不同的ViewHolder
        val item = itemStyle.getViewItem(viewType)
            ?: throw RuntimeException("itemStyle.getViewItem is null.")
        val viewBinding = item.getItemViewAny(parent.context, parent) as? ViewBinding
            ?: throw RuntimeException("itemStyle.getItemViewAny is null.")

        val viewHolder = ViewBindingHolder(viewBinding)

        //点击的监听
        if (item.openClick()) {
            setListener(viewHolder, item.openShake())
            //子view监听
            setChildListener(viewHolder, item.openShake())
        }
        return viewHolder
    }

}