package com.lee.api.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lee.api.databinding.ActivityWindowInsetsBinding
import com.lee.library.extensions.binding
import com.lee.library.extensions.toast
import com.lee.library.tools.SystemBarTools.fullWindow
import com.lee.library.tools.SystemBarTools.hasNavigationBar
import com.lee.library.tools.SystemBarTools.hasSoftInputShow
import com.lee.library.tools.SystemBarTools.hideSoftInput
import com.lee.library.tools.SystemBarTools.navigationBarHeight
import com.lee.library.tools.SystemBarTools.runWindowInsets
import com.lee.library.tools.SystemBarTools.setDarkStatusIcon
import com.lee.library.tools.SystemBarTools.setLightStatusIcon
import com.lee.library.tools.SystemBarTools.showSoftInput
import com.lee.library.tools.SystemBarTools.statusBar
import com.lee.library.tools.SystemBarTools.statusBarHeight

class WindowInsetsActivity : AppCompatActivity() {

    private val binding by binding(ActivityWindowInsetsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 设置沉浸式状态栏
        window.statusBar()

        binding.buttonStatusLightIcon.setOnClickListener {
            window.setLightStatusIcon()
        }

        binding.buttonStatusDarkIcon.setOnClickListener {
            window.setDarkStatusIcon()
        }

        binding.buttonFullWindow.setOnClickListener {
            window.fullWindow(true)
        }

        binding.buttonCancelFullWindow.setOnClickListener {
            window.fullWindow(false)
        }

        binding.buttonShowInput.setOnClickListener {
            window.showSoftInput()
        }

        binding.buttonHideInput.setOnClickListener {
            window.hideSoftInput()
        }

        binding.buttonChangeInput.setOnClickListener {
            if (window.hasSoftInputShow()) {
                window.hideSoftInput()
            } else {
                toast("change success")
            }
        }

        binding.root.runWindowInsets {
            val text = "navigationBar:${hasNavigationBar()}," +
                    "statusBarHeight:${statusBarHeight()}," +
                    "navigationBarHeight:${navigationBarHeight()}"
            binding.tvContent.text = text

        }
    }
}