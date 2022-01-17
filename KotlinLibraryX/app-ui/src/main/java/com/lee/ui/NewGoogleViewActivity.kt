package com.lee.ui

import com.lee.library.base.BaseActivity
import com.lee.library.extensions.binding
import com.lee.library.extensions.toast
import com.lee.library.widget.FloatingLayout
import com.lee.ui.databinding.ActivityNewGoogleViewBinding

/**
 * @author jv.lee
 * @date 2022/1/13
 * @description
 */
class NewGoogleViewActivity : BaseActivity() {

    private val binding by binding(ActivityNewGoogleViewBinding::inflate)

    override fun bindView() {
        binding.floatingLayout.setEventCallback(object : FloatingLayout.EventCallback() {
            override fun onClicked() {
                toast("click")
            }

            override fun onDragStart() {
            }

            override fun onDragEnd() {
            }

        })
    }

    override fun bindData() {

    }
}