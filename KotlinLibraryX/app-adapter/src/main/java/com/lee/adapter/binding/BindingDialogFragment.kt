package com.lee.adapter.binding

import com.lee.adapter.databinding.DialogBindingBinding
import com.lee.library.base.BaseBindingAlertFragment

/**
 *
 * @author jv.lee
 * @date 2021/6/16
 */
class BindingDialogFragment : BaseBindingAlertFragment<DialogBindingBinding>( true) {

    override fun bindView() {
        mBinding.tvText.text = "JV.LEE"
    }

    override fun bindData() {
    }
}