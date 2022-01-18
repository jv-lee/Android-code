package com.lee.ui

import com.lee.library.base.BaseActivity
import com.lee.library.extensions.binding
import com.lee.library.extensions.toast
import com.lee.library.utils.LogUtil
import com.lee.library.widget.FloatingLayout
import com.lee.ui.databinding.ActivityFloatingLayoutBinding

/**
 * @author jv.lee
 * @date 2022/1/18
 * @description
 */
class FloatingLayoutActivity : BaseActivity() {

    private val binding by binding(ActivityFloatingLayoutBinding::inflate)

    override fun bindView() {
        binding.floatingLayout.setEventCallback(object : FloatingLayout.EventCallback() {
            override fun onClicked() {
                toast("click")
            }

            override fun onDragStart() {
                LogUtil.i("dragStart")
            }

            override fun onDragEnd() {
                LogUtil.i("onDragEnd")
            }

        })
    }

    override fun bindData() {

    }

}