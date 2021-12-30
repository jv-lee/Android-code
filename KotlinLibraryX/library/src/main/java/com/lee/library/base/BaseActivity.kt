package com.lee.library.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lee.library.tools.StatusTools.statusBar

/**
 * @author jv.lee
 * @date 2021/6/15
 * @description
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBar()
        super.onCreate(savedInstanceState)

        initSavedState(intent, savedInstanceState)

        bindView()

        bindData()
    }

    open fun initSavedState(intent: Intent, savedInstanceState: Bundle?) {}

    protected abstract fun bindView()

    protected abstract fun bindData()

}