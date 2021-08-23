package com.lee.basedialog

import com.lee.basedialog.databinding.ActivityDialogShowBinding
import com.lee.basedialog.dialog.BaseBottomDialogImpl
import com.lee.basedialog.fragment.BaseAlertDialogFragmentImpl
import com.lee.basedialog.fragment.BaseDialogFragmentImpl
import com.lee.basedialog.fragment.BaseSheetDialogFragmentImpl
import com.lee.library.base.BaseActivity
import com.lee.library.dialog.multiple.MultipleDialogTask
import com.lee.library.extensions.binding

/**
 * @author jv.lee
 * @data 2021/8/23
 * @description 该activity代码逻辑为 app主页面多个引导弹窗任务展示管理
 */
class DialogShowActivity : BaseActivity() {

    private val binding by binding(ActivityDialogShowBinding::inflate)

    //dialogFragment
    private val baseDialogFragmentImpl by lazy { BaseDialogFragmentImpl() }
    private val baseAlertDialogFragmentImpl by lazy { BaseAlertDialogFragmentImpl() }
    private val baseSheetDialogFragmentImpl by lazy { BaseSheetDialogFragmentImpl() }

    //dialog
    private val baseBottomDialogImpl by lazy { BaseBottomDialogImpl(this) }

    //dialog任务栈 后进先出
    private val multipleDialogTask by lazy {
        MultipleDialogTask(supportFragmentManager)
            .addAction(baseDialogFragmentImpl)
            .addAction(baseAlertDialogFragmentImpl)
            .addAction(baseSheetDialogFragmentImpl) { false }
            .addAction(baseBottomDialogImpl)
    }

    override fun bindView() {
        binding.button.setOnClickListener {
            multipleDialogTask.nextShow()
        }
    }

    override fun bindData() {

    }
}