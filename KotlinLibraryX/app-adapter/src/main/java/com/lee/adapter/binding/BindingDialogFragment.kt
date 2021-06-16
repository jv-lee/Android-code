package com.lee.adapter.binding

import com.lee.adapter.R
import com.lee.adapter.databinding.DialogBindingBinding
import com.lee.library.base.BaseAlertFragment
import com.lee.library.extensions.binding

/**
 * @author jv.lee
 * @date 2021/6/16
 * @description
 */
class BindingDialogFragment : BaseAlertFragment(R.layout.dialog_binding, true) {

    val binding by binding(DialogBindingBinding::bind)

    override fun bindView() {
        binding.tvText.text = "JV.LEE"
    }

    override fun bindData() {

    }

}