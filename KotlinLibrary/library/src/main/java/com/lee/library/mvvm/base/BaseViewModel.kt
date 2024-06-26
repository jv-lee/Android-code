package com.lee.library.mvvm.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.CustomException
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val failedEvent: MutableLiveData<CustomException> = MutableLiveData()

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            block()
        }
    }

    suspend fun <T> launchIO(block: suspend CoroutineScope.() -> T) {
        withContext(Dispatchers.IO) {
            block
        }
    }

    fun launch(failedCode: Int = -1, tryBlock: suspend CoroutineScope.() -> Unit) {
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

}

