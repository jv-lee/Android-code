package com.lee.app.server

import com.lee.app.entity.BaseData
import com.lee.app.entity.Tab
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Url

/**
 *
 * @author jv.lee
 * @date 2020/3/20
 */
interface ApiService {

    @GET
    fun getTabAsync(@Url url: String): Deferred<BaseData<ArrayList<Tab>>>
}