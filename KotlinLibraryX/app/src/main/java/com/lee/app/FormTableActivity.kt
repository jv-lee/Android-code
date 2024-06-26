package com.lee.app

import com.lee.app.adapter.FormAdapter
import com.lee.app.databinding.ActivityFormTableBinding
import com.lee.library.base.BaseBindingActivity
import com.lee.library.tools.SystemBarTools.setDarkStatusIcon
import com.lee.library.tools.SystemBarTools.softInputBottomPaddingChange
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * 表单样式 沉浸式状态栏 输入法适配
 * @author jv.lee
 * @date 2020/9/7
 */
class FormTableActivity :
    BaseBindingActivity<ActivityFormTableBinding>() {
    private val adapter by lazy {
        FormAdapter(this)
    }

    override fun bindView() {
        window.setDarkStatusIcon()

        mBinding.root.softInputBottomPaddingChange()
    }

    override fun bindData() {
        adapter.addData(ArrayList<String>().also {
            for (index in 0..30) {
                it.add("this is item data -> $index")
            }
        })
    }

    @ExperimentalCoroutinesApi
    override fun onDestroy() {
        FormAdapter.dialogMap.clear()
        super.onDestroy()
    }
}
