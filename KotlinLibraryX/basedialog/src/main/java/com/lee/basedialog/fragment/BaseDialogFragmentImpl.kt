package com.lee.basedialog.fragment

import androidx.lifecycle.ViewModel
import com.lee.basedialog.R
import com.lee.basedialog.databinding.FragmentBaseDialogBinding
import com.lee.library.base.BaseDialogFragment

/**
 * @author jv.lee
 * @date 2020/9/21
 * @description
 */
class BaseDialogFragmentImpl :
    BaseDialogFragment<FragmentBaseDialogBinding, ViewModel>(R.layout.fragment_base_dialog, false) {
    override fun bindView() {

    }

    override fun bindData() {

    }
}