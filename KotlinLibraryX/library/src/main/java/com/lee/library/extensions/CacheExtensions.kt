/*
 * 缓存管理器扩展类
 * @author jv.lee
 * @date 2021/9/9
 */
package com.lee.library.extensions

import com.google.gson.reflect.TypeToken
import com.lee.library.adapter.page.PagingData
import com.lee.library.cache.CacheManager
import kotlinx.coroutines.flow.flow

/**
 * 获取缓存数据
 * @param key 缓存key
 */
inline fun <reified T> CacheManager.getCache(key: String): T? {
    val type = object : TypeToken<T>() {}.type
    return get<T>(key, type)
}

/**
 * 添加缓存数据
 * @param key 存储key
 * @param data 存储数据源
 */
inline fun <reified T> CacheManager.putCache(key: String, data: T) {
    put(key, data)
}

/**
 * @param key 存储key
 * @param data 存储数据源
 * 对分页数据扩展可控空数据不进行存储
 */
inline fun <reified T : PagingData<*>> CacheManager.putPageCache(key: String, data: T) {
    if (data.getDataSource().isEmpty()) {
        clearCache(key)
    } else {
        put(key, data)
    }
}

/**
 * 清除缓存
 * @param key 缓存key
 */
fun CacheManager.clearCache(key: String) {
    put(key, "")
}

/**
 * T 数据源转换为uiFlow数据 - 支持缓存数据
 * @param requestBlock 数据获取函数
 */
inline fun <reified T> CacheManager.cacheFlow(
    cacheKey: String,
    crossinline requestBlock: suspend () -> T? = { null },
) = flow {
    //加载缓存数据
    val data: T? = getCache<T>(cacheKey)?.also { emit(it) }

    //网络数据
    requestBlock()?.also {
        if (data != it) {
            //发送网络数据
            emit(it)
            //发送存储本地数据
            putCache(cacheKey, it)
        }
    }
}