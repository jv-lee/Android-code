package com.lee.library.mvvm.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.library.base.BaseApplication
import com.lee.library.mvvm.CustomException
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description
 */
open class BaseViewModel : ViewModel(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    val failedEvent: MutableLiveData<CustomException> = MutableLiveData()

    open fun <T : Application?> getApplication(): T {
        return BaseApplication.getContext() as T
    }

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        launch {
            block()
        }
    }

    suspend fun <T> launchIO(block: suspend CoroutineScope.() -> T) {
        withContext(Dispatchers.IO) {
            block
        }
    }

    fun launchMain(failedCode: Int = -1, tryBlock: suspend CoroutineScope.() -> Unit) {
        launchOnUI {
            tryCatch(tryBlock, {}, {}, true, failedCode)
        }
    }

    fun launchOnUITryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean
    ) {
        launchOnUI {
            tryCatch(tryBlock, catchBlock, finallyBlock, handleCancellationExceptionManually)
        }
    }

    fun launchOnUITryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false
    ) {
        launchOnUI {
            tryCatch(tryBlock, {}, {}, handleCancellationExceptionManually)
        }
    }

    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false,
        failedCode: Int = -1
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Throwable) {
                e.printStackTrace()
                if (e !is CancellationException || handleCancellationExceptionManually) {
                    failedEvent.value =
                        CustomException(failedCode, e)
                    catchBlock(e)
                } else {
                    throw e
                }
            } finally {
                finallyBlock()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancel()
    }

}

