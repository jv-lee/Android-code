package com.lee.basedialog

import android.view.WindowManager
import androidx.lifecycle.ViewModel
import com.lee.basedialog.databinding.ActivityMainBinding
import com.lee.basedialog.dialog.BaseAlertDialogImpl
import com.lee.basedialog.dialog.BaseBottomDialogImpl
import com.lee.basedialog.dialog.BaseTranslucentDialogImpl
import com.lee.basedialog.fragment.BaseAlertDialogFragmentImpl
import com.lee.basedialog.fragment.BaseDialogFragmentImpl
import com.lee.basedialog.fragment.BaseSheetDialogFragmentImpl
import com.lee.library.base.BaseVMActivity
import com.lee.library.extensions.show

class MainActivity : BaseVMActivity<ActivityMainBinding, ViewModel>(R.layout.activity_main) {

    //dialogFragment
    private val baseDialogFragmentImpl by lazy { BaseDialogFragmentImpl() }
    private val baseAlertDialogFragmentImpl by lazy { BaseAlertDialogFragmentImpl() }
    private val baseSheetDialogFragmentImpl by lazy { BaseSheetDialogFragmentImpl() }

    //dialog
    private val baseBottomDialogImpl by lazy { BaseBottomDialogImpl(this) }
    private val baseAlertDialogImpl by lazy {
        BaseAlertDialogImpl(this).also {
            //清除默认遮罩
            it.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }
    private val baseTranslucentDialogImpl by lazy { BaseTranslucentDialogImpl(this) }

    override fun bindView() {
        //普通dialogFragment
        binding.btnBaseDialogFragment.setOnClickListener {
            show(baseDialogFragmentImpl)
        }
        //alert动画的 dialogFragment
        binding.btnAlertDialogFragment.setOnClickListener {
            show(baseAlertDialogFragmentImpl)
        }
        //底部弹出sheetDialogFragment Activity根部局必须添加behavior app:layout_behavior="@string/bottom_sheet_behavior"
        binding.btnSheetDialogFragment.setOnClickListener {
            show(baseSheetDialogFragmentImpl)
        }

        //底部弹出bottomDialog
        binding.btnBottomDialog.setOnClickListener {
            show(baseBottomDialogImpl)
        }

        //alert动画 dialog
        binding.btnAlertDialogFragment.setOnClickListener {
            show(baseAlertDialogImpl)
        }

        //translucent Dialog
        binding.btnTranslucentDialog.setOnClickListener {
            show(baseTranslucentDialogImpl)
        }
    }

    override fun bindData() {
    }
}
