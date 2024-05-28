package com.lee.library.adapter.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lee.library.adapter.base.BaseViewHolder
import com.lee.library.adapter.base.BaseViewItem
import com.lee.library.adapter.binding.ViewBindingHolder
import com.lee.library.tools.ViewBindingTools

/**
 * viewBinding实现 item类型类
 * @author jv.lee
 * @date 2021/6/15
 */
abstract class ViewBindingItem<VB : ViewBinding, Data> : BaseViewItem<Data> {

    private var _binding: VB? = null
    val mBinding: VB get() = _binding!!

    override fun getItemViewAny(context: Context, parent: ViewGroup): Any {
        _binding = ViewBindingTools.inflateWithGeneric(
            this,
            LayoutInflater.from(context), parent, false
        )
        return mBinding
    }

    override fun convert(holder: BaseViewHolder, entity: Data, position: Int) {
        this.convert(holder as ViewBindingHolder, entity, position)
    }

    override fun viewRecycled(holder: BaseViewHolder) {
        this.viewRecycled(holder as ViewBindingHolder)
    }

    // Deprecated use ViewBindingUtil.inflateWithGeneric binding
    // impl ItemBinding.inflate(LayoutInflater.from(context), parent, false)
//    abstract fun getItemViewBinding(context: Context, parent: ViewGroup): ViewBinding

    abstract fun convert(holder: ViewBindingHolder, entity: Data, position: Int)

    open fun viewRecycled(holder: ViewBindingHolder) {}
}