package com.lee.app.server

import com.lee.app.entity.BaseData
import com.lee.app.entity.Tab
import com.lee.library.net.HttpManager
import com.lee.library.net.request.IRequest
import com.lee.library.net.request.Request
import kotlinx.coroutines.Deferred

/**
 *
 * @author jv.lee
 * @date 2020/3/20

 */
class ApiServiceImpl : ApiService {

    val api: ApiService by lazy {
        val request =
            Request(baseUrl = "https://www.baidu.com", converterType = IRequest.ConverterType.JSON)
        HttpManager.instance.getService(ApiService::class.java, request)
    }

    companion object {
        @Volatile
        private var instance: ApiServiceImpl? = null

        fun get() =
            instance ?: synchronized(this) {
                instance ?: ApiServiceImpl().also { instance = it }
            }
    }

    override fun getTabAsync(url: String): Deferred<BaseData<ArrayList<Tab>>> {
        return api.getTabAsync(url)
    }
}