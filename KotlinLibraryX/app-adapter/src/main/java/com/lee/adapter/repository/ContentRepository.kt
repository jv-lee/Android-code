package com.lee.adapter.repository

import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.PageData
import com.lee.library.mvvm.base.BaseRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart

/**
 * @author jv.lee
 * @date 2020/11/25
 * @description
 */
class ContentRepository : BaseRepository() {

    fun getContent(page: Int): PageData<ContentData> {
        return PageData(page, 3, arrayListOf<ContentData>().also {
            for (index in 1..10) {
                it.add(ContentData(page.toLong()))
            }
        })
    }

    fun getFlowContent(page:Int):Flow<PageData<ContentData>>{
        return flowOf(PageData(page, 3, arrayListOf<ContentData>().also {
            for (index in 1..10) {
                it.add(ContentData(page.toLong()))
            }
        })).onStart {
            delay(500)
        }
    }

}