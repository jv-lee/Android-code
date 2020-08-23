package com.lee.library.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.lee.library.mvvm.base.BaseViewModel
import com.lee.library.utils.StatusUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description
 */
abstract class BaseFullActivity<V : ViewDataBinding, VM : ViewModel>(
    layoutId: Int) : BaseActivity<V,VM>(layoutId)
    , CoroutineScope by CoroutineScope(Dispatchers.Main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusUtil.fullWindow(this,true)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        //防止横竖屏切换影响全屏状态
        StatusUtil.fullWindow(this,true)
    }

}