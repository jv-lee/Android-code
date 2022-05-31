package com.lee.basedialog.fragment

import androidx.lifecycle.ViewModel
import com.lee.basedialog.R
import com.lee.basedialog.databinding.FragmentAlertDialogBinding
import com.lee.library.base.BaseVMAlertFragment

/**
 * Alert样式 DialogFragment
 * @author jv.lee
 * @date 2020/9/21
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