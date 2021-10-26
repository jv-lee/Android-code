package com.lee.library.mvvm.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author jv.lee
 * @data 2021/10/26
 * @description
 */

data class UiAction<T>(val value: T, val version: Double = Math.random())

@Suppress("FunctionName")
fun <T> MutableStateActionFlow(value: T): MutableStateFlow<UiAction<T>> =
    MutableStateFlow(UiAction(value))

typealias StateActionFlow<T> = StateFlow<UiAction<T>>

inline fun <reified T> MutableStateFlow<UiAction<T>>.emitAction(value: T) {
    tryEmit(UiAction(value))
}

