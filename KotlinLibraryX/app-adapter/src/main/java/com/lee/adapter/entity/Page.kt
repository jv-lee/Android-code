package com.lee.adapter.entity

import com.lee.library.adapter.page.PagingData

data class Page<T>(
    val data: ArrayList<T>,
    val status: Int = 0,
    val page: Int,
    val page_count: Int
) : PagingData<T> {
    override fun getPageNumber(): Int {
        return page
    }

    override fun getPageTotalNumber(): Int {
        return page_count
    }

    override fun getDataSource(): MutableList<T> {
        return data
    }

}