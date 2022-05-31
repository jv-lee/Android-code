package com.lee.library.tools

/**
 *
 * @author jv.lee
 * @date 2021/1/12
 *
 */
object ReflexTools {
    inline fun <reified T> reflexField(clazz: Any, fieldName: String): T? {
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

    fun reflexMethod(clazz: Any, methodName: String) {
        try {
            val method = clazz.javaClass.getDeclaredMethod(methodName)
            method.invoke(clazz)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}