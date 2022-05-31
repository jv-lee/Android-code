package com.lee.library.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lee.library.tools.StatusTools.compatStatusBar
import com.lee.library.tools.StatusTools.statusBar

/**
 * Activity通用基类
 * @author jv.lee
 * @date 2021/6/15
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBar()
        window.compatStatusBar()
        super.onCreate(savedInstanceState)

        initSavedState(intent, savedInstanceState)

        bindView()

        bindData()
    }

    open fun initSavedState(intent: Intent, savedInstanceState: Bundle?) {}

    protected abstract fun bindView()

    protected abstract fun bindData()

}