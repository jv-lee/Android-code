package com.lee.library.adapter.page

/**
 * 分页数据源实现接口 根据分页page实现
 * @author jv.lee
 * @date 2020/8/11
 */
interface PagingData<T> {
    fun getPageNumber():Int
    fun getPageTotalNumber():Int
    fun getDataSource(): MutableList<T>
}