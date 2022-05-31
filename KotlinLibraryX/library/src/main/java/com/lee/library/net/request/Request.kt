package com.lee.library.net.request

import androidx.annotation.NonNull

/**
 *
 * @author jv.lee
 * @date 2020/3/20
 */
class Request constructor(
    @NonNull override val baseUrl: String,
    @IRequest.ConverterType override val converterType: Int = IRequest.ConverterType.STRING,
    val converterTypes: IntArray? = null,
    @IRequest.CallType override val callType: Int = IRequest.CallType.COROUTINE,
    val callTypes: IntArray? = null,
    override val isDownload: Boolean = false,
    override val key: String = baseUrl + callType + converterType
) : IRequest