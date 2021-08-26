package com.lee.basedialog.fragment

import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lee.basedialog.R
import com.lee.basedialog.databinding.FragmentSheetDialogBinding
import com.lee.library.base.BaseVMSheetFragment
import com.lee.library.dialog.core.DialogActionCall

/**
 * @author jv.lee
 * @date 2020/9/21
 * @description Activity根部局必须添加 app:layout_behavior="@string/bottom_sheet_behavior"
 */
class BaseSheetDialogFragmentImpl : BaseVMSheetFragment<FragmentSheetDialogBinding, ViewModel>(
    R.layout.fragment_sheet_dialog,
    true,
    BottomSheetBehavior.STATE_EXPANDED,
    130
) {

    var actionCall: DialogActionCall? = null

    override fun bindView() {

    }

    override fun bindData() {

    }

}