package com.lee.basedialog.fragment

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lee.basedialog.databinding.FragmentSheetDialogBinding
import com.lee.library.base.BaseBindingSheetFragment

/**
 * Activity根部局必须添加 app:layout_behavior="@string/bottom_sheet_behavior"
 * @author jv.lee
 * @date 2020/9/21
 */
class BaseSheetDialogFragmentImpl : BaseBindingSheetFragment<FragmentSheetDialogBinding>(
    true, BottomSheetBehavior.STATE_EXPANDED, 130
) {

    var onConfirm: (() -> Unit)? = null
    var onCancel: (() -> Unit)? = null

    override fun bindView() {
    }

    override fun bindData() {
    }
}