package com.lee.library.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import com.lee.library.mvvm.base.BaseViewModel
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException

/**
 * @author jv.lee
 * @date 2021/9/22
 * @description
 */
open class CoroutineViewModel : BaseViewModel() {

    fun launchMain(tryBlock: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            tryCatch(tryBlock, {}, {}, true)
        }
    }

    fun launchIO(tryBlock: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            tryCatch(tryBlock, {}, {}, true)
        }
    }

    suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T {
        return withContext(Dispatchers.IO) {
            block()
        }
    }

    fun launchOnUITryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean
    ) {
        viewModelScope.launch {
            tryCatch(tryBlock, catchBlock, finallyBlock, handleCancellationExceptionManually)
        }
    }

    fun launchOnUITryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false
    ) {
        viewModelScope.launch {
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
                    failedEvent.postValue(e)
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