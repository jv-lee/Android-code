package com.lee.library.dialog.intercept

/**
 * Dialog拦截器 每个拦截器调用下一个拦截器的拦截方法
 * @author jv.lee
 * @date 2021/8/26
 */
abstract class DialogIntercept<T> {
    var next: DialogIntercept<T>? = null

    open fun intercept(item: T) {
        next?.intercept(item)
    }
}