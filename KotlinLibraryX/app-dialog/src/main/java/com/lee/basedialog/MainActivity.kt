package com.lee.basedialog

import android.view.WindowManager
import android.widget.Button
import com.lee.basedialog.databinding.ActivityMainBinding
import com.lee.basedialog.dialog.BaseAlertDialogImpl
import com.lee.basedialog.dialog.BaseBottomDialogImpl
import com.lee.basedialog.dialog.BaseTranslucentDialogImpl
import com.lee.basedialog.fragment.BaseAlertDialogFragmentImpl
import com.lee.basedialog.fragment.BaseDialogFragmentImpl
import com.lee.basedialog.fragment.BaseSheetDialogFragmentImpl
import com.lee.library.base.BaseVMActivity
import com.lee.library.mvvm.base.BaseViewModel

class MainActivity : BaseVMActivity<ActivityMainBinding, BaseViewModel>(R.layout.activity_main) {

    //dialogFragment
    private val baseDialogFragmentImpl by lazy { BaseDialogFragmentImpl() }
    private val baseAlertDialogFragmentImpl by lazy { BaseAlertDialogFragmentImpl() }
    private val baseSheetDialogFragmentImpl by lazy { BaseSheetDialogFragmentImpl() }

    //dialog
    private val baseBottomDialogImpl by lazy { BaseBottomDialogImpl(this) }
    private val baseAlertDialogImpl by lazy { BaseAlertDialogImpl(this).also {
        //清除默认遮罩
        it.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    } }
    private val baseTranslucentDialogImpl by lazy { BaseTranslucentDialogImpl(this) }

    override fun bindView() {
        //普通dialogFragment
        findViewById<Button>(R.id.btn_base_dialog_fragment).setOnClickListener {
            show(baseDialogFragmentImpl)
        }
        //alert动画的 dialogFragment
        findViewById<Button>(R.id.btn_alert_dialog_fragment).setOnClickListener {
            show(baseAlertDialogFragmentImpl)
        }
        //底部弹出sheetDialogFragment Activity根部局必须添加behavior app:layout_behavior="@string/bottom_sheet_behavior"
        findViewById<Button>(R.id.btn_sheet_dialog_fragment).setOnClickListener {
            show(baseSheetDialogFragmentImpl)
        }

        //底部弹出bottomDialog
        findViewById<Button>(R.id.btn_bottom_dialog).setOnClickListener {
            show(baseBottomDialogImpl)
        }

        //alert动画 dialog
        findViewById<Button>(R.id.btn_alert_dialog).setOnClickListener {
            show(baseAlertDialogImpl)
        }

        //translucent Dialog
        findViewById<Button>(R.id.btn_translucent_dialog).setOnClickListener {
            show(baseTranslucentDialogImpl)
        }
    }

    override fun bindData() {
    }
}
