package com.example.padding3.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.example.padding3.model.entity.Content
import com.lee.library.utils.LogUtil
import java.lang.Exception

class ContentDataSource : PagingSource<Int, Content>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Content> {
        return try {
            val page = params.key ?: 1
            //获取网络数据
            val result =
                ApiRepository.getApi().getContentDataAsync("All", "Flutter", page, params.loadSize).await()
            LoadResult.Page(
                data = result.data,
                prevKey = null,
                nextKey = if (result.page == result.page_count) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        fun load() = Pager(PagingConfig(pageSize = 20)) { ContentDataSource() }.flow
    }

}