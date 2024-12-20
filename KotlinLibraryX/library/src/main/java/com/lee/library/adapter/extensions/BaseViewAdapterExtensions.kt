/*
 * [BaseViewAdapter] 扩展函数类
 * @author jv.lee
 * @date 2022/1/11
 */
@file:Suppress("UNCHECKED_CAST")

package com.lee.library.adapter.extensions

import androidx.recyclerview.widget.DiffUtil
import com.lee.library.adapter.base.BaseViewAdapter
import com.lee.library.adapter.callback.DiffCallback
import com.lee.library.adapter.paging.PagingData
import com.lee.library.adapter.paging.PagingData2

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

/**
 * 提交分页数据填充，控制页面显示状态及加载状态
 * @param pageData 数据源
 * @param limit 首页限制数
 * @param diff 是否自动diff新旧数据
 * @param refreshBlock 刷新状态回调函数
 * @param emptyBlock 空数据状态回调函数
 */
fun <T> BaseViewAdapter<T>.submitData(
    pageData: PagingData<T>,
    limit: Int = 1,
    diff: Boolean = false,
    refreshBlock: () -> Unit = {},
    emptyBlock: () -> Unit = {}
) {
    // 首页加载逻辑
    if (pageData.getPageNumber() == limit) {
        openLoadMore()

        // 过滤首页重复数据
        if (getData() == pageData.getDataSource()) {
            // 重复数据空数据校验
            if (pageData.getDataSource().isEmpty()) {
                if (isPageCompleted()) initStatusView()
                clearData()
                pageEmpty()
                emptyBlock()
                return
            }

            // 刷新后首页数据即是末尾页
            if (pageData.getPageNumber() >= pageData.getPageTotalNumber()) {
                loadMoreEnd()
                return
            }

            // 过滤重复数据
            return
        }

        // 设置空页面
        if (pageData.getDataSource().isEmpty()) {
            if (isPageCompleted()) initStatusView()
            clearData()
            pageEmpty()
            emptyBlock()
            return
        }

        // 数据源不同替换数据更改状态
        if (getData() != pageData.getDataSource()) {
            // 正常情况第一页加载数据状态
            updateData(pageData.getDataSource())
            pageCompleted()
            refreshBlock()
        }

        // 分页加载逻辑
    } else {
        // 数据相同不处理
        if (getData() == pageData.getDataSource()) {
            return
        }

        // 防止view重构后在分页加载时 pageCompleted状态重置
        if (!isPageCompleted()) {
            pageCompleted()
        }

        if (diff) {
            // 防止activity重建在viewModel中填充历史数据 做差分填充
            val oldData = getData()
            updateData(pageData.getDataSource())
            val result =
                DiffUtil.calculateDiff(DiffCallback<T>(oldData, pageData.getDataSource()), true)
            result.dispatchUpdatesTo(this)
        } else {
            addData(pageData.getDataSource())
        }
    }

    // 设置尾页状态 (包括notifyDateSetChange)
    if (pageData.getPageNumber() >= pageData.getPageTotalNumber()) {
        loadMoreEnd()
    } else {
        loadMoreCompleted()
    }
}

/**
 * 提交分页数据填充，控制页面显示状态及加载状态 [PagingData2] 方式的
 * @param pageData 数据源
 * @param diff 是否自动diff新旧数据
 * @param refreshBlock 刷新状态回调函数
 * @param emptyBlock 空数据状态回调函数
 */
fun <T> BaseViewAdapter<T>.submitData(
    pageData: PagingData2<T>,
    diff: Boolean = false,
    refreshBlock: () -> Unit = {},
    emptyBlock: () -> Unit = {}
) {
    // 首页加载逻辑
    if (pageData.isFirstPage()) {
        openLoadMore()

        // 过滤首页重复数据
        if (getData() == pageData.getDataSource()) {
            // 重复数据空数据校验
            if (pageData.getDataSource().isEmpty()) {
                if (isPageCompleted()) initStatusView()
                clearData()
                pageEmpty()
                emptyBlock()
                return
            }
            // 刷新后首页数据即是末尾页
            if (pageData.isLastPage()) {
                loadMoreEnd()
                return
            }
            // 过滤重复数据
            return
        }

        // 设置空页面
        if (pageData.getDataSource().isEmpty()) {
            if (isPageCompleted()) initStatusView()
            clearData()
            pageEmpty()
            emptyBlock()
            return
        }

        // 数据源不同替换数据更改状态
        if (getData() != pageData.getDataSource()) {
            // 正常情况第一页加载数据状态
            updateData(pageData.getDataSource())
            pageCompleted()
            refreshBlock()
        }

        // 分页加载逻辑
    } else {
        // 数据相同不处理
        if (getData() == pageData.getDataSource()) {
            return
        }

        // 防止view重构后在分页加载时 pageCompleted状态重置
        if (!isPageCompleted()) {
            pageCompleted()
        }

        if (diff) {
            // 防止activity重建在viewModel中填充历史数据 做差分填充
            val oldData = getData()
            updateData(pageData.getDataSource())
            val result =
                DiffUtil.calculateDiff(DiffCallback<T>(oldData, pageData.getDataSource()), true)
            result.dispatchUpdatesTo(this)
        } else {
            addData(pageData.getDataSource())
        }
    }

    // 设置尾页状态 (包括notifyDateSetChange)
    if (pageData.isLastPage()) {
        loadMoreEnd()
    } else {
        loadMoreCompleted()
    }
}

/**
 * 提交单页面填充数据、无分页内容
 * @param newData 数据源
 */
fun <T> BaseViewAdapter<T>.submitSinglePage(newData: List<T>) {
    if (getData().isNullOrEmpty() && newData.isEmpty()) {
        pageEmpty()
    } else if (newData.isNotEmpty()) {
        updateData(newData)
        pageCompleted()
        loadMoreEnd()
    }
}

/**
 * 提交错误状态设置页面状态
 */
fun <T> BaseViewAdapter<T>.submitFailed() {
    if (isPageCompleted() && !isItemEnd() && getData().isNotEmpty()) {
        loadFailed()
    } else if (!isPageCompleted() && getData().isEmpty()) {
        pageError()
    }
}