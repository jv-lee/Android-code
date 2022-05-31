package com.lee.basedialog

import com.lee.basedialog.databinding.ActivityDialogShowBinding
import com.lee.basedialog.dialog.BaseBottomDialogImpl
import com.lee.basedialog.fragment.BaseAlertDialogFragmentImpl
import com.lee.basedialog.fragment.BaseDialogFragmentImpl
import com.lee.basedialog.fragment.BaseSheetDialogFragmentImpl
import com.lee.basedialog.intercept.FourDialogIntercept
import com.lee.basedialog.intercept.OneDialogIntercept
import com.lee.basedialog.intercept.ThreeDialogIntercept
import com.lee.basedialog.intercept.TwoDialogIntercept
import com.lee.library.base.BaseActivity
import com.lee.library.dialog.intercept.DialogCreateConfig
import com.lee.library.dialog.intercept.DialogInterceptHandler
import com.lee.library.dialog.multiple.MultipleDialogTask
import com.lee.library.extensions.binding

/**
 * 该activity代码逻辑为 app主页面多个引导弹窗任务展示管理
 * @author jv.lee
 * @date 2021/8/23
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

    //dialog拦截器模式 根据拦截器内部条件显示
    private val interceptDialogs by lazy {
        DialogInterceptHandler<DialogCreateConfig>().apply {
            add(OneDialogIntercept())
            add(TwoDialogIntercept())
            add(ThreeDialogIntercept())
            add(FourDialogIntercept())
        }
    }

    override fun bindView() {
        //多dialog弹窗加载任务
        binding.button.setOnClickListener {
            multipleDialogTask.nextShow()
        }
        //拦截器复杂弹窗加载任务
        binding.buttonIntercept.setOnClickListener {
            interceptDialogs.intercept(DialogCreateConfig(this, supportFragmentManager))
        }
    }

    override fun bindData() {

    }
}