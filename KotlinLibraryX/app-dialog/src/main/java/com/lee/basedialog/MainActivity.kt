package com.lee.basedialog

import com.lee.basedialog.databinding.ActivityMainBinding
import com.lee.basedialog.dialog.BaseAlertDialogImpl
import com.lee.basedialog.dialog.BaseBottomDialogImpl
import com.lee.basedialog.dialog.BaseTranslucentDialogImpl
import com.lee.basedialog.fragment.BaseAlertDialogFragmentImpl
import com.lee.basedialog.fragment.BaseDialogFragmentImpl
import com.lee.basedialog.fragment.BaseSheetDialogFragmentImpl
import com.lee.library.base.BaseBindingActivity
import com.lee.library.dialog.extensions.hideCover
import com.lee.library.extensions.show

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    // dialogFragment
    private val baseDialogFragmentImpl by lazy { BaseDialogFragmentImpl() }
    private val baseAlertDialogFragmentImpl by lazy { BaseAlertDialogFragmentImpl() }
    private val baseSheetDialogFragmentImpl by lazy { BaseSheetDialogFragmentImpl() }

    // dialog
    private val baseBottomDialogImpl by lazy { BaseBottomDialogImpl(this) }
    private val baseAlertDialogImpl by lazy { BaseAlertDialogImpl(this).hideCover() }
    private val baseTranslucentDialogImpl by lazy { BaseTranslucentDialogImpl(this) }

    override fun bindView() {
        // 普通dialogFragment
        mBinding.btnBaseDialogFragment.setOnClickListener {
            show(baseDialogFragmentImpl)
        }
        // alert动画的 dialogFragment
        mBinding.btnAlertDialogFragment.setOnClickListener {
            show(baseAlertDialogFragmentImpl)
        }
        // 底部弹出sheetDialogFragment Activity根部局必须添加behavior app:layout_behavior="@string/bottom_sheet_behavior"
        mBinding.btnSheetDialogFragment.setOnClickListener {
            show(baseSheetDialogFragmentImpl)
        }

        // 底部弹出bottomDialog
        mBinding.btnBottomDialog.setOnClickListener {
            show(baseBottomDialogImpl)
        }

        // alert动画 dialog
        mBinding.btnAlertDialog.setOnClickListener {
            show(baseAlertDialogImpl)
        }

        // translucent Dialog
        mBinding.btnTranslucentDialog.setOnClickListener {
            show(baseTranslucentDialogImpl)
        }
    }

    override fun bindData() {
    }
}
