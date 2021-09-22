package com.lee.library.mvvm.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.lee.library.mvvm.livedata.SingleLiveData
import com.lee.library.net.HttpManager

/**
 * @author jv.lee
 * @date 2020/5/21
 * @description
 */
open class BaseLiveData<T> : MutableLiveData<T>() {
    private val failedEvent: SingleLiveData<Throwable> = SingleLiveData()

    fun throwMessage(throwable: Throwable) {
        failedEvent.setValue(throwable)
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in T>, failedObserver: Observer<Throwable>) {
        super.observe(owner, observer)
        failedEvent.observe(owner, failedObserver)
    }

}