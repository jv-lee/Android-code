package com.lee.library.extensions

import android.app.Activity
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment

/**
 * @author jv.lee
 * @date 2020/8/19
 * @description  activity/fragment 获取intent参数扩展函数
 */

@MainThread
inline fun <reified P : Any> Activity.arguments(key: String): Lazy<P> =
    getIntentParams(key)

@MainThread
inline fun <reified P : Any> Fragment.arguments(key: String): Lazy<P> =
    getArgumentsParams(key)

@MainThread
inline fun <reified P : Any> Fragment.activityArguments(key: String): Lazy<P> =
    getActivityIntentParams(key)

@MainThread
inline fun <reified P : Any> Activity.getIntentParams(
    key: String
): Lazy<P> {
    return ParamsLazy {
        checkNotNull(intent.extras) { "activity intent.extras is null." }
        intent.extras?.getValue(key) as P
    }
}

@MainThread
inline fun <reified P : Any> Fragment.getActivityIntentParams(key: String): Lazy<P> {
    return ParamsLazy {
        checkNotNull(requireActivity().intent.extras) { "requestActivity().intent.extras is null." }
        requireActivity().intent.extras?.getValue(key) as P
    }
}

@MainThread
inline fun <reified P : Any> Fragment.getArgumentsParams(
    key: String
): Lazy<P> {
    return ParamsLazy {
        checkNotNull(arguments) { "fragment arguments is null." }
        arguments?.getValue(key) as P
    }
}

class ParamsLazy<P : Any>(
    private val initializer: () -> P
) : Lazy<P> {
    private var cached: P? = null

    override val value: P
        get() {
            if (cached == null) {
                cached = initializer()
            }

            return cached as P
        }

    override fun isInitialized(): Boolean = cached != null
}

inline fun <reified T> Bundle.getValue(key: String): T {
    return when (T::class.java) {
        java.lang.Integer::class.java -> getInt(key, 0) as T
        java.lang.Long::class.java -> getLong(key, 0L) as T
        java.lang.String::class.java -> getString(key, "") as T
        java.lang.Boolean::class.java -> getBoolean(key, false) as T
        java.lang.Float::class.java -> getFloat(key, 0f) as T
        else -> throw RuntimeException("not type support.")
    }
}