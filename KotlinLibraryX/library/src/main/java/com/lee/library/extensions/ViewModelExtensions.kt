/*
 * viewModel扩展函数帮助类
 * @author jv.lee
 * @date 2020/4/1
 */

@file:Suppress("UNCHECKED_CAST")

package com.lee.library.extensions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import java.lang.reflect.Constructor
import java.lang.reflect.ParameterizedType

/**
 * 获取当前class第二个泛型class类型
 */
fun <VM> getVmClass(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as VM
}

//设置SavedStateHandle为参数的ViewModel构造函数类型 最终创建ViewModel
typealias CreateViewModel = (handle: SavedStateHandle) -> ViewModel

/**
 * activity的SavedStateHandlerViewModel扩展函数
 */
@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModelByFactory(
): Lazy<VM> {
    return viewModels {
        createViewModelFactory(this, intent.extras) {
            val constructor = findMatchingConstructor<VM>(arrayOf(SavedStateHandle::class.java))
            checkNotNull(constructor)
            constructor.newInstance(it)
        }
    }
}

/**
 * fragment的SavedStateHandlerViewModel扩展函数
 */
inline fun <reified VM : ViewModel> Fragment.viewModelByFactory(): Lazy<VM> {
    return viewModels {
        createViewModelFactory(this, arguments) {
            val constructor = findMatchingConstructor<VM>(arrayOf(SavedStateHandle::class.java))
            checkNotNull(constructor)
            constructor.newInstance(it)
        }
    }
}

/**
 * fragment的SavedStateHandlerViewModel扩展函数
 */
inline fun <reified VM : ViewModel> Fragment.activityViewModelByFactory(
    noinline create: CreateViewModel
): Lazy<VM> {
    return activityViewModels {
        createViewModelFactory(this, arguments, create)
    }
}

/**
 * 实际SavedStateViewModel 工厂构建方法
 * @param owner 当前状态生命周期管理
 * @param defaultArgs 默认参数集
 * @param create 创建ViewModel类型
 */
@MainThread
fun createViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle?,
    create: CreateViewModel
): ViewModelProvider.Factory {
    //通过抽象SavedStateViewModelFactory创建viewModel工厂
    return object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            @Suppress("UNCHECKED_CAST")
            return create(handle) as? T
                ?: throw IllegalArgumentException("Unknown viewModel class!")
        }
    }
}

/**
 * 通过class查找当前类构造函数
 * @param signature 当前构造参数类型数组
 * T 当前传入class类型
 */
@PublishedApi
@MainThread
internal inline fun <reified T> findMatchingConstructor(
    signature: Array<Class<*>>
): Constructor<T>? {
    //遍历当前class的所有构造函数
    for (constructor in T::class.java.constructors) {
        //获取当前构造函数参数类型
        val parameterTypes = constructor.parameterTypes
        //参数类型与目标参数class类型匹配则为目标构造函数 返回构造函数
        if (signature.contentEquals(parameterTypes)) {
            @Suppress("UNCHECKED_CAST")
            return constructor as Constructor<T>
        }
    }
    return null
}