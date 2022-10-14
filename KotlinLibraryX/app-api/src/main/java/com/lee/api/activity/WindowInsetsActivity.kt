package com.lee.api.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lee.api.databinding.ActivityWindowInsetsBinding
import com.lee.library.extensions.binding
import com.lee.library.tools.StatusTools.fullWindow
import com.lee.library.tools.StatusTools.hasNavigationBar
import com.lee.library.tools.StatusTools.navigationBarHeight
import com.lee.library.tools.StatusTools.runWindowInsets
import com.lee.library.tools.StatusTools.setDarkStatusIcon
import com.lee.library.tools.StatusTools.setLightStatusIcon
import com.lee.library.tools.StatusTools.statusBar
import com.lee.library.tools.StatusTools.statusBarHeight

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

        window.runWindowInsets {
            binding.tvContent.text =
                "navigationBar:${hasNavigationBar()},statusBarHeight:${statusBarHeight()},navigationBarHeight:${navigationBarHeight()}"
        }
    }

}