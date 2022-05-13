package com.lee.adapter.repository

import com.lee.adapter.entity.Content
import com.lee.adapter.entity.Page
import com.lee.adapter.server.ApiService
import com.lee.library.net.HttpManager
import com.lee.library.net.request.IRequest
import com.lee.library.net.request.Request
import kotlinx.coroutines.flow.Flow

/**
 * @author jv.lee
 * @date 2020/11/26
 * @description
 */
class FlowRepository {

    private val api: ApiService

    init {
        val request = Request(
            "https://gank.io/api/v2/",
            IRequest.ConverterType.JSON,
            callType = IRequest.CallType.FLOW
        )
        api = HttpManager.instance.getService(ApiService::class.java, request)
    }

    fun getData(page: Int): Flow<Page<Content>> {
        return api.getContentDataAsync(page)
    }
}