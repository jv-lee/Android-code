package com.lee.library.net.request

import androidx.annotation.NonNull

/**
 * @author jv.lee
 * @date 2020/3/20
 * @description
 */
class Request constructor(
    @NonNull override val baseUrl: String,
    @IRequest.ConverterType override val converterType: Int = IRequest.ConverterType.STRING,
    @IRequest.CallType override val callType: Int = IRequest.CallType.COROUTINE,
    override val isDownload: Boolean = false,
    override val key: String = baseUrl + callType + converterType
) : IRequest