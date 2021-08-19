package com.lee.library.extensions

import android.app.Activity
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
fun <P : Any> Activity.getIntentParams(
    key: String
): Lazy<P> {
    return ParamsLazy {
        checkNotNull(intent.extras) { "activity intent.extras is null." }
        val value = intent.extras.get(key)
        checkNotNull(value) { "activity intent.extras query $key value is not found." }
        value as P
    }
}

@MainThread
fun <P : Any> Fragment.getActivityIntentParams(key: String): Lazy<P> {
    return ParamsLazy {
        checkNotNull(requireActivity().intent.extras) { "requestActivity().intent.extras is null." }
        val value = requireActivity().intent.extras.get(key)
        checkNotNull(value) { "requestActivity().intent.extras query $key value is not found." }
        value as P
    }
}

@MainThread
fun <P : Any> Fragment.getArgumentsParams(
    key: String
): Lazy<P> {
    return ParamsLazy {
        checkNotNull(arguments) { "fragment arguments is null." }
        val value = arguments?.get(key)
        checkNotNull(value) { "fragment arguments query $key value is not found." }
        value as P
    }
}

class ParamsLazy<P : Any>(
    private val initializer: () -> P
) : Lazy<P> {
    private var cached: P? = null

    override val value: P
        get() {
            if (cached == null) {
                cached = initializer.invoke()
            }

            return cached as P
        }

    override fun isInitialized(): Boolean = cached != null
}