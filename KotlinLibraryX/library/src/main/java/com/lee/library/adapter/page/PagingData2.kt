package com.lee.library.adapter.page

/**
 * 分页数据源实现接口2 根据分页key实现
 * @author jv.lee
 * @date 2020/8/11
 */
interface PagingData2<T> {
    fun isFirstPage(): Boolean
    fun isLastPage(): Boolean
    fun getDataSource(): List<T>
}