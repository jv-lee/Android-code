package com.lee.basedialog.fragment

import com.lee.basedialog.databinding.FragmentAlertDialogBinding
import com.lee.library.base.BaseBindingAlertFragment

/**
 * Alert样式 DialogFragment
 * @author jv.lee
 * @date 2020/9/21
 */
class BaseAlertDialogFragmentImpl :
    BaseBindingAlertFragment<FragmentAlertDialogBinding>(isCancel = true, isFullWindow = false) {

    override fun bindView() {
    }

    override fun bindData() {
    }
}