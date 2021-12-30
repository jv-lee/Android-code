package com.lee.library.mvvm.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.lee.library.base.ApplicationExtensions.app
import com.lee.library.mvvm.livedata.SingleLiveData

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description
 */
open class BaseViewModel : ViewModel() {

    val failedEvent = SingleLiveData<Throwable>()

    open fun <T : Application?> getApplication(): Context {
        return app
    }

}

