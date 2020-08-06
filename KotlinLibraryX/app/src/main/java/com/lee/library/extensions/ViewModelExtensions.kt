package com.lee.library.extensions

import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
fun <VM> getVmClass(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as VM
}