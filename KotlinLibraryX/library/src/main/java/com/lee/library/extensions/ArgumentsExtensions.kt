/*
 * activity/fragment 获取intent参数扩展函数
 * @author jv.lee
 * @date 2020/8/19
 */
package com.lee.library.extensions

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment

@MainThread
inline fun <reified P : Any> Activity.arguments(key: String): Lazy<P> =
    getIntentParams(key)

@MainThread
inline fun <reified P : Any> Activity.argumentsOrNull(key: String): Lazy<P?> =
    getIntentParamsOrNull(key)

@MainThread
inline fun <reified P : Parcelable> Activity.argumentsList(key: String): Lazy<ArrayList<P>> =
    getIntentParamsList(key)

@MainThread
inline fun <reified P : Any> Fragment.arguments(key: String): Lazy<P> =
    getArgumentsParams(key)

@MainThread
inline fun <reified P : Any> Fragment.argumentsOrNull(key: String): Lazy<P?> =
    getArgumentsOrNullParams(key)

@MainThread
inline fun <reified P : Parcelable> Fragment.argumentsList(key: String): Lazy<ArrayList<P>> =
    getArgumentsParamsList(key)

@MainThread
inline fun <reified P : Any> Fragment.activityArguments(key: String): Lazy<P> =
    getActivityIntentParams(key)

@MainThread
inline fun <reified P : Any> Fragment.activityArgumentsOrNull(key: String): Lazy<P?> =
    getActivityIntentParamsOrNull(key)

@MainThread
inline fun <reified P : Parcelable> Fragment.activityArgumentsList(key: String):
    Lazy<ArrayList<P>> = getActivityIntentParamsList(key)

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
inline fun <reified P : Any> Activity.getIntentParamsOrNull(
    key: String
): Lazy<P?> {
    return ParamsOrNullLazy {
        checkNotNull(intent.extras) { "activity intent.extras is null." }
        intent.extras?.getValueOrNull(key) as? P
    }
}

@MainThread
inline fun <reified P : Parcelable> Activity.getIntentParamsList(key: String): Lazy<ArrayList<P>> {
    return ParamsListLazy {
        checkNotNull(intent.extras) { "activity intent.extras is null." }
        intent.extras?.getValueList<P>(key) as ArrayList<P>
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
inline fun <reified P : Any> Fragment.getActivityIntentParamsOrNull(key: String): Lazy<P?> {
    return ParamsOrNullLazy {
        checkNotNull(requireActivity().intent.extras) { "requestActivity().intent.extras is null." }
        requireActivity().intent.extras?.getValueOrNull(key) as? P
    }
}

@MainThread
inline fun <reified P : Parcelable> Fragment.getActivityIntentParamsList(key: String):
    Lazy<ArrayList<P>> {
    return ParamsListLazy {
        checkNotNull(requireActivity().intent.extras) { "requestActivity().intent.extras is null." }
        requireActivity().intent.extras?.getValueList<P>(key) as ArrayList<P>
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

@MainThread
inline fun <reified P : Any> Fragment.getArgumentsOrNullParams(key: String): Lazy<P?> {
    return ParamsOrNullLazy {
        checkNotNull(arguments) { "fragment arguments is null." }
        arguments?.getValueOrNull<P>(key)
    }
}

@MainThread
inline fun <reified P : Parcelable> Fragment.getArgumentsParamsList(
    key: String
): Lazy<ArrayList<P>> {
    return ParamsListLazy {
        checkNotNull(arguments) { "fragment arguments is null." }
        arguments?.getValueList<P>(key) as ArrayList<P>
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

class ParamsOrNullLazy<P : Any>(
    private val initializer: () -> P?
) : Lazy<P?> {
    private var cached: P? = null

    override val value: P?
        get() {
            if (cached == null) {
                cached = initializer()
            }

            return cached
        }

    override fun isInitialized(): Boolean = cached != null
}

class ParamsListLazy<P : Any>(private val initializer: () -> ArrayList<P>) : Lazy<ArrayList<P>> {
    private var cached: ArrayList<P>? = null

    override val value: ArrayList<P>
        get() {
            if (cached == null) {
                cached = initializer()
            }

            return cached as ArrayList<P>
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

inline fun <reified T> Bundle.getValueOrNull(key: String): T? {
    return when (T::class.java) {
        java.lang.Integer::class.java -> getInt(key, 0) as T
        java.lang.Long::class.java -> getLong(key, 0L) as T
        java.lang.String::class.java -> getString(key, "") as T
        java.lang.Boolean::class.java -> getBoolean(key, false) as T
        java.lang.Float::class.java -> getFloat(key, 0f) as T

        else -> {
            when {
                T::class.java.interfaces.contains(java.io.Serializable::class.java) -> {
                    getSerializableCompat(key) as T?
                }
                T::class.java.interfaces.contains(android.os.Parcelable::class.java) -> {
                    getParcelableCompat(key) as T?
                }
                else -> {
                    null
                }
            }
        }
    }
}

inline fun <reified T : Parcelable> Bundle.getValueList(key: String): ArrayList<T> {
    if (T::class.java.interfaces.contains(android.os.Parcelable::class.java)) {
        return getParcelableArrayListCompat(key)
    } else {
        throw RuntimeException("not type support.")
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : java.io.Serializable> Bundle.getSerializableCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, T::class.java)
    } else {
        getSerializable(key) as? T
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        getParcelable(key) as? T
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.getParcelableArrayListCompat(key: String): ArrayList<T> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayList(key, T::class.java) ?: arrayListOf()
    } else {
        getParcelableArrayList<T>(key) as? ArrayList<T> ?: arrayListOf()
    }
}