package com.lee.adapter.repository

import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.PageData
import com.lee.adapter.entity.ViewType

/**
 *
 * @author jv.lee
 * @date 2020/11/25
 */
class ContentRepository {

    fun getContent(page: Int): PageData<ContentData> {
        return PageData(
            page,
            3,
            arrayListOf<ContentData>().also {
                for (index in 1..12) {
                    when (index) {
                        11 -> it.add(ContentData(page.toLong(), ViewType.VERTICAL))
                        12 -> it.add(ContentData(page.toLong(), ViewType.HORIZONTAL))
                        else -> it.add(ContentData(page.toLong(), ViewType.COMMON))
                    }
                }
            }
        )
    }
}