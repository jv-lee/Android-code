package com.lee.basedialog.fragment

import androidx.lifecycle.ViewModel
import com.lee.basedialog.R
import com.lee.basedialog.databinding.FragmentAlertDialogBinding
import com.lee.library.base.BaseVMAlertFragment

/**
 * @author jv.lee
 * @date 2020/9/21
 * @description Alert样式 DialogFragment
 */
class BaseAlertDialogFragmentImpl :
    BaseVMAlertFragment<FragmentAlertDialogBinding, ViewModel>(
        R.layout.fragment_alert_dialog,
        isCancel = true,
        isFullWindow = false
    ) {

    override fun bindView() {

    }

    override fun bindData() {

    }
}