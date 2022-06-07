@file:Suppress("UNCHECKED_CAST")
package com.lee.library.adapter.binding

import androidx.viewbinding.ViewBinding
import com.lee.library.adapter.base.BaseViewHolder

/**
 * @author jv.lee
 * @date 2019/3/29
 * [BaseViewHolder] 的viewBinding实现 ,使用viewBinding解析view时可使用该ViewHolder类
 */
class ViewBindingHolder(private val binding: ViewBinding) : BaseViewHolder(binding.root) {

    fun <VB : ViewBinding> getViewBinding() = binding as VB

}