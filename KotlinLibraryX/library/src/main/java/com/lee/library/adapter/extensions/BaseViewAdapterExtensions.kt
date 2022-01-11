package com.lee.library.adapter.extensions

import com.lee.library.adapter.base.BaseViewAdapter
import com.lee.library.adapter.listener.LoadResource

/**
 * @author jv.lee
 * @date 2022/1/11
 * @description
 */

fun <T> BaseViewAdapter<T>.bindAllListener(cThis: Any) {
    setOnItemClickListener(cThis as? BaseViewAdapter.OnItemClickListener<T>)
    setOnItemLongClickListener(cThis as? BaseViewAdapter.OnItemLongClickListener<T>)
    setOnItemChildClickListener(cThis as? BaseViewAdapter.OnItemChildView<T>)
    setLoadErrorListener(cThis as? BaseViewAdapter.LoadErrorListener)
    setAutoLoadMoreListener(cThis as? BaseViewAdapter.AutoLoadMoreListener)
    setLoadResource(cThis as? LoadResource)
}