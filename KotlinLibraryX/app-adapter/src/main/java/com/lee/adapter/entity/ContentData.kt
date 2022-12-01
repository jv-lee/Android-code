package com.lee.adapter.entity

import com.lee.library.adapter.page.PagingData

/**
 *
 * @author jv.lee
 * @date 2020/11/25
 */
data class ContentData(val id: Long = 1, @ViewType val type: Int)

annotation class ViewType {
    companion object {
        const val COMMON = 0
        const val VERTICAL = 1
        const val HORIZONTAL = 2
    }
}

data class PageData<T>(
    val page: Int,
    val pageTotal: Int,
    val data: MutableList<T>
) : PagingData<T> {

    override fun getPageNumber() = page

    override fun getPageTotalNumber() = pageTotal

    override fun getDataSource() = data
}