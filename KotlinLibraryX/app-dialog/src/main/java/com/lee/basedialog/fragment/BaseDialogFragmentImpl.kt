package com.lee.basedialog.fragment

import androidx.lifecycle.ViewModel
import com.lee.basedialog.R
import com.lee.basedialog.databinding.FragmentBaseDialogBinding
import com.lee.library.base.BaseVMDialogFragment

/**
 * BaseDialog样式 DialogFragment
 * @author jv.lee
 * @date 2020/9/21
 */
class BaseDialogFragmentImpl :
    BaseVMDialogFragment<FragmentBaseDialogBinding, ViewModel>(R.layout.fragment_base_dialog) {
    override fun bindView() {
    }

    override fun bindData() {
    }
}