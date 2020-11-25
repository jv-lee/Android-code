package com.lee.adapter.entity

import com.lee.library.adapter.page.PagingData

/**
 * @author jv.lee
 * @date 2020/11/25
 * @description
 */
data class ContentData(val id: Long = 1)

data class PageData<T>(val page: Int, val pageTotal: Int, val data: List<T>) : PagingData<T> {

    override fun getPageNumber() = page

    override fun getPageTotalNumber() = pageTotal

    override fun getDataSource() = data

}