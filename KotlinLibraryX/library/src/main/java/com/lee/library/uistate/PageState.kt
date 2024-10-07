package com.lee.library.uistate

import androidx.annotation.IntDef
import com.lee.library.uistate.PageState.Companion.DATA
import com.lee.library.uistate.PageState.Companion.EMPTY
import com.lee.library.uistate.PageState.Companion.ERROR
import com.lee.library.uistate.PageState.Companion.LOADING
import com.lee.library.uistate.PageState.Companion.NETWORK
import com.lee.library.uistate.PageState.Companion.UNKNOWN

/**
 * 页面加载状态值
 *
 * [LOADING] 加载中状态
 * [EMPTY] 空状态
 * [ERROR] 错误状态
 * [NETWORK] 无网络状态
 * [DATA] 数据显示状态
 *
 * @author jv.lee
 * @date 2024/9/10
 */
@IntDef(LOADING, EMPTY, ERROR, NETWORK, DATA, UNKNOWN)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class PageState {
    companion object {
        const val UNKNOWN: Int = 0

        /** 加载状态 */
        const val LOADING: Int = 1

        /** 空数据 */
        const val EMPTY: Int = 2

        /** 错误数据 */
        const val ERROR: Int = 3

        /** 无网络 */
        const val NETWORK: Int = 4

        /** 数据展示 */
        const val DATA: Int = 5
    }
}