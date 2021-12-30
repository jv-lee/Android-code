package com.lee.app

import com.lee.app.adapter.FormAdapter
import com.lee.app.databinding.ActivityFormTableBinding
import com.lee.library.base.BaseVMActivity
import com.lee.library.mvvm.base.BaseViewModel
import com.lee.library.tools.KeyboardHelper
import com.lee.library.tools.KeyboardTools.keyboardOpenMoveView
import com.lee.library.tools.StatusTools.setDarkStatusIcon
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author jv.lee
 * @date 2020/9/7
 * @description 表单样式 沉浸式状态栏 输入法适配
 */
class FormTableActivity :
    BaseVMActivity<ActivityFormTableBinding, BaseViewModel>(R.layout.activity_form_table) {
    private val adapter by lazy {
        FormAdapter(this, ArrayList<String>().also {
            for (index in 0..30) {
                it.add("this is item data -> $index")
            }
        })
    }

    private val keyboardHelper by lazy { KeyboardHelper(window.decorView, binding.root) }

    override fun bindView() {
        setDarkStatusIcon()
//        keyboardHelper.enable()
        window.keyboardOpenMoveView(binding.constRoot)

//        binding.rvContainer.layoutManager = LinearLayoutManager(this)
//        binding.rvContainer.adapter = adapter
    }

    override fun bindData() {

    }

    @ExperimentalCoroutinesApi
    override fun onDestroy() {
        FormAdapter.dialogMap.clear()
//        keyboardHelper.disable()
        super.onDestroy()
    }

}