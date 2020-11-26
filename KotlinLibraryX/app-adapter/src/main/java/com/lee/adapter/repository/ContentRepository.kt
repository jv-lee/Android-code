package com.lee.adapter.repository

import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.PageData
import com.lee.library.mvvm.base.BaseRepository

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

}