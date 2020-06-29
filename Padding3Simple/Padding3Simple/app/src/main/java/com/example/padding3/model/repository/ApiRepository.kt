package com.example.padding3.model.repository

import com.example.padding3.http.ApiService
import com.lee.library.net.HttpManager
import com.lee.library.net.request.IRequest
import com.lee.library.net.request.Request

/**
 * @author jv.lee
 * @date 2020/3/24
 * @description 数据提供类 服务提供及数据库内容提供
 */
class ApiRepository {

    private val api: ApiService

    init {
        val request =
            Request("https://gank.io/api/v2/", IRequest.ConverterType.JSON)
        api = HttpManager.getInstance().getService(ApiService::class.java, request)
    }

    companion object {

        @Volatile
        private var instance: ApiRepository? = null

        @JvmStatic
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ApiRepository().also { instance = it }
        }

        fun getApi(): ApiService {
            return getInstance().api
        }

    }

}