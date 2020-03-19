package com.lee.coroutine.server

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author jv.lee
 * @date 2020/3/19
 * @description
 */
interface ApiService {
    @GET("users/{login}")
    fun getUserAsync(@Path("login") login: String): Deferred<String>

    @GET("users/{login}")
    suspend fun getUserAsync2(@Path("login") login: String): String
}