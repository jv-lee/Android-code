package com.lee.coroutine.test

import androidx.lifecycle.MutableLiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lee.coroutine.server.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

/**
 * @author jv.lee
 * @date 2020/3/19
 * @description  1.普通携程的使用
 */
class PlanACommon {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val api = retrofit.create(ApiService::class.java)

    val userData = MutableLiveData<String>()
    val userError = MutableLiveData<Exception>()

    /**
     * 通过携程使用Retrofit 回调携程 1.普通
     */
    fun request() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                userData.value = api.getUserAsync("jv.lee").await()
            } catch (e: Exception) {
                userError.value = e
            }
        }
    }

    /**
     * 通过携程使用Retrofit 回调String 2.suspend 修饰接口
     */
    fun request2() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                userData.value = api.getUserAsync2("jv.lee")
            } catch (e: Exception) {
                userError.value = e
            }
        }
    }
}