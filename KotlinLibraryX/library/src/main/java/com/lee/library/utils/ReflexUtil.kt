package com.lee.library.utils

/**
 * @author jv.lee
 * @date 2021/1/12
 * @description
 */
object ReflexUtil {
    fun <T> reflexField(clazz: Any, fieldName: String): T? {
        try {
            val field = clazz.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            val `object` = field[clazz]
            return `object` as T
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }
}