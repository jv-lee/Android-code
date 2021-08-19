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
public inline fun <reified P : Any> Activity.arguments(key: String): Lazy<P> = getIntentParams(key)

@MainThread
public inline fun <reified P : Any> Fragment.arguments(key: String): Lazy<P> = getIntentParams(key)

@MainThread
public fun <P : Any> Activity.getIntentParams(
    key: String
): Lazy<P> {
    return ParamsLazy {
        checkNotNull(intent.extras) { "activity intent.extras is null." }
        val value = intent.extras.get(key)
        checkNotNull(value) { "activity bundle by key:$key is not found." }
        value as P
    }
}

@MainThread
public fun <P : Any> Fragment.getIntentParams(
    key: String
): Lazy<P> {
    return ParamsLazy {
        checkNotNull(arguments) { "fragment arguments is null." }
        val value = arguments?.get(key)
        checkNotNull(value) { "fragment bundle by key:$key is not found." }
        value as P
    }
}

public class ParamsLazy<P : Any>(
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