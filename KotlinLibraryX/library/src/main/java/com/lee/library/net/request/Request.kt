package com.lee.library.net.request

/**
 * 请求参数组合实现类
 * @param baseUrl 服务地址
 * @param converterType 默认请求返回转换数据类型
 * @param converterTypes 支持多种返回转换类型，如果不设置使用默认converterType
 * @param callType 默认请求返回包裹类型
 * @param callTypes 支持多种返回包裹类型，如果不设置使用默认callType
 * @param isDownload 是否是下载请求
 * @param key 请求key默认以 baseUrl + callType + converterType 设置
 * @author jv.lee
 * @date 2020/3/20
 */
class Request constructor(
    override val baseUrl: String,
    @IRequest.ConverterType override val converterType: Int = IRequest.ConverterType.STRING,
    val converterTypes: IntArray? = null,
    @IRequest.CallType override val callType: Int = IRequest.CallType.COROUTINE,
    val callTypes: IntArray? = null,
    override val isDownload: Boolean = false,
    override val key: String = baseUrl + callType + converterType
) : IRequest