package com.lee.app

import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.app.adapter.FormAdapter
import com.lee.app.databinding.ActivityFormTableBinding
import com.lee.library.base.BaseActivity
import com.lee.library.mvvm.base.BaseViewModel
import com.lee.library.utils.KeyboardHelper
import com.lee.library.utils.KeyboardUtil
import com.lee.library.utils.StatusUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author jv.lee
 * @date 2020/9/7
 * @description 表单样式 沉浸式状态栏 输入法适配
 */
class FormTableActivity :
    BaseActivity<ActivityFormTableBinding, BaseViewModel>(R.layout.activity_form_table) {
    private val adapter by lazy {
        FormAdapter(this, ArrayList<String>().also {
            for (index in 0..30) {
                it.add("this is item data -> $index")
            }
        })
    }

    private val keyboardHelper by lazy { KeyboardHelper(window.decorView, binding.root) }

    override fun bindView() {
        StatusUtil.setStatusFontLight2(this)
//        keyboardHelper.enable()
        KeyboardUtil.keyboardOpenMoveView(window, binding.constRoot)

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