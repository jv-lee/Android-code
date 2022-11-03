package com.lee.library.viewstate

import androidx.annotation.IntDef
import com.lee.library.viewstate.LoadStatus.Companion.INIT
import com.lee.library.viewstate.LoadStatus.Companion.LOAD_MORE
import com.lee.library.viewstate.LoadStatus.Companion.REFRESH
import com.lee.library.viewstate.LoadStatus.Companion.RELOAD
import kotlin.random.Random

/**
 * PageUiState 分页ui状态类 支持 flow/liveData 扩展使用
 * @param requestFirstPage 分页初始请求页码
 * @param responseFirstPage 分页数据返回首页页码
 * @author jv.lee
 * @date 2022/2/14
 */
sealed class UiStatePage(
    var requestFirstPage: Int = 1,
    var responseFirstPage: Int = 1
) {
    var page = requestFirstPage
    var firstCache = true

    class Default constructor(
        requestFirstPage: Int = 1,
        responseFirstPage: Int = 1
    ) : UiStatePage(requestFirstPage, responseFirstPage)

    data class Success<T>(
        val data: T,
        var version: Int = Random.nextInt()
    ) : UiStatePage()

    data class Failure<T>(
        val data: T?,
        val exception: Throwable,
        var version: Int = Random.nextInt()
    ) : UiStatePage()

    data class Error(
        val exception: Throwable
    ) : UiStatePage()
}

fun UiStatePage.copy(data: UiStatePage): UiStatePage {
    data.requestFirstPage = requestFirstPage
    data.responseFirstPage = responseFirstPage
    data.page = page
    data.firstCache = firstCache
    return data
}

inline fun <reified T> UiStatePage.call(
    crossinline success: (T) -> Unit,
    crossinline error: (Throwable) -> Unit,
    crossinline default: () -> Unit = {}
) {
    when (this) {
        is UiStatePage.Success<*> -> success(this.data as T)
        is UiStatePage.Error -> error(this.exception)
        is UiStatePage.Failure<*> -> {
            (this.data as T?)?.run(success)
            error(this.exception)
        }
        else -> {
            default()
        }
    }
}

@IntDef(INIT, REFRESH, LOAD_MORE, RELOAD)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class LoadStatus {

    companion object {
        const val INIT: Int = 0x0000
        const val REFRESH: Int = 0x001
        const val LOAD_MORE: Int = 0x002
        const val RELOAD: Int = 0x003
    }
}