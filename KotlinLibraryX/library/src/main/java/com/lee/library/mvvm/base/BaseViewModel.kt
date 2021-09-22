package com.lee.library.mvvm.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.library.base.BaseApplication

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description
 */
open class BaseViewModel : ViewModel() {

    val failedEvent: MutableLiveData<Throwable> = MutableLiveData()

    open fun <T : Application?> getApplication(): Context {
        return BaseApplication.getContext()
    }

}

