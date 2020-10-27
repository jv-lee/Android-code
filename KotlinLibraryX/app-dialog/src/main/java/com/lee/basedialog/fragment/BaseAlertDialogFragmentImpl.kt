package com.lee.basedialog.fragment

import androidx.lifecycle.ViewModel
import com.lee.basedialog.R
import com.lee.basedialog.databinding.FragmentAlertDialogBinding
import com.lee.library.base.BaseAlertDialogFragment
import com.lee.library.utils.StatusUtil

/**
 * @author jv.lee
 * @date 2020/9/21
 * @description Alert样式 DialogFragment
 */
class BaseAlertDialogFragmentImpl :
    BaseAlertDialogFragment<FragmentAlertDialogBinding, ViewModel>(
        R.layout.fragment_alert_dialog,
        true
    ) {
    override fun bindView() {
        StatusUtil.statusBar(dialog?.window!!,false)
    }

    override fun bindData() {

    }
}