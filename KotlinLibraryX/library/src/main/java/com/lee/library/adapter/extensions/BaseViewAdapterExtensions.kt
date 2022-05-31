package com.lee.library.adapter.extensions

import com.lee.library.adapter.base.BaseViewAdapter
import com.lee.library.adapter.listener.LoadResource

/**
 *
 * @author jv.lee
 * @date 2022/1/11
 */

@Suppress("UNCHECKED_CAST")
inline fun <reified T> BaseViewAdapter<T>.bindAllListener(cThis: Any) {
    (cThis as? BaseViewAdapter.OnItemClickListener<T>)?.run(this::setOnItemClickListener)
    (cThis as? BaseViewAdapter.OnItemLongClickListener<T>)?.run(this::setOnItemLongClickListener)
    (cThis as? BaseViewAdapter.OnItemChildView<T>)?.run(this::setOnItemChildClickListener)
    (cThis as? BaseViewAdapter.LoadErrorListener)?.run(this::setLoadErrorListener)
    (cThis as? BaseViewAdapter.AutoLoadMoreListener)?.run(this::setAutoLoadMoreListener)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> BaseViewAdapter<T>.unbindAllListener() {
    setOnItemClickListener(null)
    setOnItemLongClickListener(null)
    setOnItemChildClickListener(null)
    setLoadErrorListener(null)
    setAutoLoadMoreListener(null)
}