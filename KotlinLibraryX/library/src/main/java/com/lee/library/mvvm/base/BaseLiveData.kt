package com.lee.library.mvvm.base

import androidx.lifecycle.MutableLiveData
import com.lee.library.mvvm.CustomException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * @author jv.lee
 * @date 2020/5/21
 * @description
 */
open class BaseLiveData<T> : MutableLiveData<T>(),
    CoroutineScope by CoroutineScope(Dispatchers.Main) {
    val failedEvent: MutableLiveData<CustomException> = MutableLiveData()

    fun launchMain(tryBlock: suspend CoroutineScope.() -> Unit) {
        launch {
            try {
                tryBlock()
            } catch (e: Exception) {
                failedEvent.value = CustomException(-1, e)
            }
        }

    }

    override fun onInactive() {
        super.onInactive()
        cancel()
    }


}