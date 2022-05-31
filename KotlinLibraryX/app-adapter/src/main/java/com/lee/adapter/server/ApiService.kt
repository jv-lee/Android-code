package com.lee.adapter.server

import com.lee.adapter.entity.Content
import com.lee.adapter.entity.Page
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author jv.lee
 * @date 2020/11/26
 */
interface ApiService {
    /**
     *  分类数据 API
     * @param category 可接受参数 All(所有分类) | Article | GanHuo | Girl
     * @param type 可接受参数 All(全部类型) | Android | iOS | Flutter | Girl ...，即分类API返回的类型数据
     * @param page: >=1
     * @param count： [10, 50]
     */
    @GET("data/category/Girl/type/Girl/page/{page}/count/20")
    fun getContentDataAsync(@Path("page") page: Int): Flow<Page<Content>>
}