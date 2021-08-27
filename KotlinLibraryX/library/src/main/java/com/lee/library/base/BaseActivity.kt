package com.lee.library.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lee.library.utils.StatusUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel

/**
 * @author jv.lee
 * @date 2021/6/15
 * @description
 */
abstract class BaseActivity :
    AppCompatActivity(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusUtil.statusBar(window, false)
        super.onCreate(savedInstanceState)

        initSavedState(intent, savedInstanceState)

        bindView()

        bindData()
    }

    open fun initSavedState(intent: Intent, savedInstanceState: Bundle?) {}

    protected abstract fun bindView()

    protected abstract fun bindData()

    @ExperimentalCoroutinesApi
    override fun onDestroy() {
        super.onDestroy()
        cancel()

    }

}