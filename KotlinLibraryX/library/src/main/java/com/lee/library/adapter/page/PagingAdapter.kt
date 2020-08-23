package com.lee.library.adapter.page

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import com.lee.library.adapter.LeeViewAdapter
import com.lee.library.adapter.callback.DiffCallback

/**
 * @author jv.lee
 * @date 2020/8/11
 * @description
 */
open class PagingAdapter<T>(context: Context, data: ArrayList<T>) : LeeViewAdapter<T>(context, data) {

    fun submitData(
        pageData: PagingData<T>,
        limit: Int = 1,
        diff: Boolean = false,
        refreshBlock: () -> Unit = {},
        emptyBlock: () -> Unit = {}
    ) {
        if (pageData.getPageNumber() == limit) {
            //设置空页面
            if (pageData.getDataSource().isNullOrEmpty()) {
                pageEmpty()
                emptyBlock()
                return
            }
            //数据源相同不做任何操作
            if (data == pageData.getDataSource()) {
                return
            }
            //正常情况第一页加载数据状态
            updateData(pageData.getDataSource())
            pageCompleted()
            refreshBlock()
        } else {
            //防止view重构后在分页加载时 pageCompleted状态重置
            if (!isPageCompleted) {
                pageCompleted()
            }
            if (diff) {
                //防止activity重建在viewModel中填充历史数据 做差分填充
                val oldData = data
                updateData(pageData.getDataSource())
                val result = DiffUtil.calculateDiff(DiffCallback<T>(oldData, pageData.getDataSource()), true)
                result.dispatchUpdatesTo(this)
            } else {
                addData(pageData.getDataSource())
            }
        }

        //设置尾页状态 (包括notifyDateSetChange)
        if (pageData.getPageNumber() >= pageData.getPageTotalNumber()) {
            loadMoreEnd()
        } else {
            loadMoreCompleted()
        }
    }

    fun submitFailed() {
        if (isPageCompleted && data.isNotEmpty()) {
            loadFailed()
        } else if (!isPageCompleted && data.isEmpty()) {
            pageError()
        }
    }

}