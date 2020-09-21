package com.lee.basedialog

import android.widget.Button
import androidx.lifecycle.ViewModel
import com.lee.basedialog.databinding.ActivityMainBinding
import com.lee.basedialog.fragment.BaseAlertDialogFragmentImpl
import com.lee.basedialog.fragment.BaseDialogFragmentImpl
import com.lee.basedialog.fragment.BaseSheetDialogFragmentImpl
import com.lee.library.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding, ViewModel>(R.layout.activity_main) {

    private val baseDialogFragmentImpl by lazy { BaseDialogFragmentImpl() }
    private val baseAlertDialogFragmentImpl by lazy { BaseAlertDialogFragmentImpl() }
    private val baseSheetDialogFragmentImpl by lazy { BaseSheetDialogFragmentImpl() }

    override fun bindView() {
        findViewById<Button>(R.id.btn_base_dialog_fragment).setOnClickListener {
            baseDialogFragmentImpl.show(
                supportFragmentManager,
                BaseDialogFragmentImpl::class.java.simpleName
            )
        }
        findViewById<Button>(R.id.btn_alert_dialog_fragment).setOnClickListener {
            baseAlertDialogFragmentImpl.show(
                supportFragmentManager,
                BaseAlertDialogFragmentImpl::class.java.simpleName
            )
        }
        // Activity根部局必须添加behavior app:layout_behavior="@string/bottom_sheet_behavior"
        findViewById<Button>(R.id.btn_sheet_dialog_fragment).setOnClickListener {
            baseSheetDialogFragmentImpl.show(
                supportFragmentManager,
                BaseSheetDialogFragmentImpl::class.java.simpleName
            )
        }
    }

    override fun bindData() {
    }
}
