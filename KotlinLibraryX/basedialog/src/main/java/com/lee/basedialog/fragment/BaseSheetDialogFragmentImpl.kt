package com.lee.basedialog.fragment

import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lee.basedialog.R
import com.lee.basedialog.databinding.FragmentSheetDialogBinding
import com.lee.library.base.BaseSheetFragment

/**
 * @author jv.lee
 * @date 2020/9/21
 * @description Activity根部局必须添加 app:layout_behavior="@string/bottom_sheet_behavior"
 */
class BaseSheetDialogFragmentImpl :
    BaseSheetFragment<FragmentSheetDialogBinding, ViewModel>(
        R.layout.fragment_sheet_dialog,
        isFullWindow = true,
        behaviorState = BottomSheetBehavior.STATE_EXPANDED,
        peekHeight = 130
    ) {
    override fun bindView() {

    }

    override fun bindData() {

    }
}