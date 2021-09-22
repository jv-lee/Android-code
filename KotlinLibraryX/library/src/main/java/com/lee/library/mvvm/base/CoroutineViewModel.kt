package com.lee.library.mvvm.base

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException

/**
 * @author jv.lee
 * @data 2021/9/22
 * @description
 */
open class CoroutineViewModel : BaseViewModel() {

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            block()
        }
    }

    suspend fun <T> launchIO(block: suspend CoroutineScope.() -> T): T {
        return withContext(Dispatchers.IO) {
            block()
        }
    }

    fun launchMain(tryBlock: suspend CoroutineScope.() -> Unit) {
        launchOnUI {
            tryCatch(tryBlock, {}, {}, true)
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
        handleCancellationExceptionManually: Boolean = false
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Throwable) {
                e.printStackTrace()
                if (e !is CancellationException || handleCancellationExceptionManually) {
                    failedEvent.value = e
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