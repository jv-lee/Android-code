package com.lee.library.mvvm.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.lee.library.net.HttpManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @author jv.lee
 * @date 2020/5/21
 * @description
 */
open class BaseLiveData<T> : MutableLiveData<T>(),
    CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val failedEvent: MutableLiveData<String> = MutableLiveData()

    fun throwMessage(throwable: Throwable) {
        failedEvent.value = HttpManager.getInstance().getServerMessage(throwable)
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in T>, failedObserver: Observer<String>) {
        super.observe(owner, observer)
        failedEvent.observe(owner, failedObserver)
    }

    fun launchMain(tryBlock: suspend CoroutineScope.() -> Unit) {
        launch {
            try {
                tryBlock()
            } catch (e: Exception) {
                failedEvent.value = e.message ?: e.toString()
            }
        }

    }

    override fun onInactive() {
        super.onInactive()
        cancel()
    }


}