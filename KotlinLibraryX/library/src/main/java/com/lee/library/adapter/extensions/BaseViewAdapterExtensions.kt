/*
 * [BaseViewAdapter] 扩展函数类
 * @author jv.lee
 * @date 2022/1/11
 */
@file:Suppress("UNCHECKED_CAST")

package com.lee.library.adapter.extensions

import com.lee.library.adapter.base.BaseViewAdapter

/**
 * [BaseViewAdapter] 绑定所有监听接口，根据 [cThis] 参数获取接口实现
 */
inline fun <reified T> BaseViewAdapter<T>.bindAllListener(cThis: Any) {
    (cThis as? BaseViewAdapter.OnItemClickListener<T>)?.run(this::setOnItemClickListener)
    (cThis as? BaseViewAdapter.OnItemLongClickListener<T>)?.run(this::setOnItemLongClickListener)
    (cThis as? BaseViewAdapter.OnItemChildView<T>)?.run(this::setOnItemChildClickListener)
    (cThis as? BaseViewAdapter.LoadErrorListener)?.run(this::setLoadErrorListener)
    (cThis as? BaseViewAdapter.AutoLoadMoreListener)?.run(this::setAutoLoadMoreListener)
}

/**
 * [BaseViewAdapter] 移除所有绑定接口
 */
inline fun <reified T> BaseViewAdapter<T>.unbindAllListener() {
    setOnItemClickListener(null)
    setOnItemLongClickListener(null)
    setOnItemChildClickListener(null)
    setLoadErrorListener(null)
    setAutoLoadMoreListener(null)
}