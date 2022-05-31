package com.lee.library.net.request

import androidx.annotation.IntDef
import com.lee.library.net.request.IRequest.CallType.Companion.COROUTINE
import com.lee.library.net.request.IRequest.CallType.Companion.FLOW
import com.lee.library.net.request.IRequest.CallType.Companion.NO
import com.lee.library.net.request.IRequest.CallType.Companion.OBSERVABLE
import com.lee.library.net.request.IRequest.ConverterType.Companion.JSON
import com.lee.library.net.request.IRequest.ConverterType.Companion.PROTO
import com.lee.library.net.request.IRequest.ConverterType.Companion.STRING

/**
 *
 * @author jv.lee
 * @date 2020/3/20
 */
interface IRequest {

    /**
     * 数据转换器类型
     */
    @Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FIELD,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.CONSTRUCTOR,
        AnnotationTarget.PROPERTY_SETTER
    )
    @IntDef(STRING, JSON, PROTO)
    @Retention(AnnotationRetention.SOURCE)
    @MustBeDocumented
    annotation class ConverterType {
        companion object {
            const val STRING = 0
            const val JSON = 1
            const val PROTO = 2
        }
    }

    val baseUrl: String?

    /**
     * 数据返回包裹类型
     */
    @Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FIELD,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.CONSTRUCTOR
    )
    @IntDef(COROUTINE, FLOW, OBSERVABLE, NO)
    @Retention(AnnotationRetention.SOURCE)
    @MustBeDocumented
    annotation class CallType {
        companion object {
            const val COROUTINE = 1
            const val FLOW = 2
            const val OBSERVABLE = 3
            const val NO = -1
        }
    }

    val converterType: Int

    val callType: Int

    val isDownload: Boolean

    val key: String?
}