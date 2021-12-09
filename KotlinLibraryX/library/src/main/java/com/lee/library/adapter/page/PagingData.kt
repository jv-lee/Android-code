package com.lee.library.adapter.page

/**
 * @author jv.lee
 * @date 2020/8/11
 * @description
 */
interface PagingData<T> {
    fun getPageNumber():Int
    fun getPageTotalNumber():Int
    fun getDataSource(): MutableList<T>
}