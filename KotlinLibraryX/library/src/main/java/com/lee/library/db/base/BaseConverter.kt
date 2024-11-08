package com.lee.library.db.base

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.lee.library.net.HttpManager

/**
 * 序列化数据 [T] 类型列表转json基础类
 * @author jv.lee
 * @date 2020/4/17
 */
open class BaseConverter<T> {
    @TypeConverter
    fun stringJsonToList(value: String?): List<T>? {
        return HttpManager.getGson().fromJson(value, object : TypeToken<List<T>>() {}.type)
    }

    @TypeConverter
    fun stringListFormJson(list: List<T>?): String? {
        return HttpManager.getGson().toJson(list)
    }
}