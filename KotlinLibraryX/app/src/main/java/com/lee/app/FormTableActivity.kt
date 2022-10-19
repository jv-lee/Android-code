package com.lee.app

import androidx.lifecycle.ViewModel
import com.lee.app.adapter.FormAdapter
import com.lee.app.databinding.ActivityFormTableBinding
import com.lee.library.base.BaseVMActivity
import com.lee.library.tools.SystemBarTools.softInputBottomPaddingChange
import com.lee.library.tools.SystemBarTools.setDarkStatusIcon
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * 表单样式 沉浸式状态栏 输入法适配
 * @author jv.lee
 * @date 2020/9/7
 */
class FormTableActivity :
    BaseVMActivity<ActivityFormTableBinding, ViewModel>(R.layout.activity_form_table) {
    private val adapter by lazy {
        FormAdapter(this, ArrayList<String>().also {
            for (index in 0..30) {
                it.add("this is item data -> $index")
            }
        })
    }

    override fun bindView() {
        window.setDarkStatusIcon()

        window.softInputBottomPaddingChange()
    }

    override fun bindData() {

    }

    @ExperimentalCoroutinesApi
    override fun onDestroy() {
        FormAdapter.dialogMap.clear()
        super.onDestroy()
    }

}