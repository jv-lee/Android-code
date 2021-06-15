package com.lee.library.adapter.binding

import androidx.viewbinding.ViewBinding
import com.lee.library.adapter.base.BaseViewHolder

/**
 * @author jv.lee
 * @date 2019/3/29
 * 封装的RecyclerViewHolder
 */
class ViewBindingHolder(private val binding: ViewBinding) : BaseViewHolder(binding.root) {

    @Suppress("UNCHECKED_CAST")
    fun <VB : ViewBinding> getViewBinding() = binding as VB

}