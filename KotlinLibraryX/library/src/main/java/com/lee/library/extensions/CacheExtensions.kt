package com.lee.library.extensions

import com.google.gson.reflect.TypeToken
import com.lee.library.cache.CacheManager
import kotlinx.coroutines.CompletableDeferred

/**
 * @author jv.lee
 * @date 2021/9/9
 * @description
 */
/**
 * @param key 缓存key
 */
suspend inline fun <reified T> CacheManager.getCache(key: String): T? {
    val type = object : TypeToken<T>() {}.type
    return CompletableDeferred(CacheManager.getDefault().get<T>(key, type)).await()
}

/**
 * @param key 存储key
 * @param data 存储数据源
 */
fun <T> CacheManager.putCache(key: String, data: T) {
    CacheManager.getDefault().put(key, data)
}

