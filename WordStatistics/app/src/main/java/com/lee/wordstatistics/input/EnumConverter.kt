package com.lee.wordstatistics.input

/**
 * Author: xiaoshilin
 * Date: 2020/2/21
 */
abstract class EnumConverter<in V, E : Enum<E>>(
        private val valueMap: Map<V, E>
) {
    fun fromValue(value: V): E? = valueMap[value]
    fun fromValue(value: V?, default: E): E = valueMap[value] ?: default
}

inline fun <V, reified E : Enum<E>> buildValueMap(keySelector: (E) -> V): Map<V, E> =
        enumValues<E>().associateBy(keySelector)

inline fun <V, reified E> buildValueMap(): Map<V, E> where E : Enum<E>, E : HasValue<V> =
        enumValues<E>().associateBy { it.value }

interface HasValue<out T> {
    val value: T
}