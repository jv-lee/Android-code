package com.lee.api.adapter

import androidx.viewbinding.ViewBinding
import com.lee.library.adapter.LeeViewHolder

/**
 * @author jv.lee
 * @date 2019/3/29
 * 封装的RecyclerViewHolder
 */
class ViewBindingHolder(private val binding: ViewBinding) : LeeViewHolder(binding.root) {

    @Suppress("UNCHECKED_CAST")
    fun <VB : ViewBinding> getViewBinding() = binding as VB

}