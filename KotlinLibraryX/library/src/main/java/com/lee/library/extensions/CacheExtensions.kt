package com.lee.library.extensions

import com.google.gson.reflect.TypeToken
import com.lee.library.adapter.page.PagingData
import com.lee.library.cache.CacheManager
import kotlinx.coroutines.CompletableDeferred

/**
 * 缓存管理器扩展类
 * @author jv.lee
 * @date 2021/9/9
 */

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
 * 对分页数据扩展可控空数据不进行存储
 * @param key 存储key
 * @param data 存储数据源
 */
inline fun <reified T : PagingData<*>> CacheManager.putPageCache(key: String, data: T) {
    if (!data.getDataSource().isNullOrEmpty()) {
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
