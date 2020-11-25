package com.lee.library.mvvm.live

import androidx.annotation.IntDef
import com.lee.library.mvvm.base.BaseLiveData
import com.lee.library.mvvm.live.LoadStatus.Companion.INIT
import com.lee.library.mvvm.live.LoadStatus.Companion.LOAD_MORE
import com.lee.library.mvvm.live.LoadStatus.Companion.REFRESH
import com.lee.library.mvvm.live.LoadStatus.Companion.RELOAD
import kotlinx.coroutines.CoroutineScope

/**
 * @author jv.lee
 * @date 2020/5/20
 * @description
 */
class PageLiveData<T>(val limit: Int = 0) : BaseLiveData<T>() {

    var page = limit
    private var firstCache = true

    fun pageLaunch(
        @LoadStatus status: Int,
        networkBlock: suspend CoroutineScope.(Int) -> T? = { page: Int -> null },
        cacheBlock: suspend CoroutineScope.() -> T? = { null },
        cacheSaveBlock: suspend CoroutineScope.(T) -> Unit = {}
    ) {
        launchMain {
            var response: T? = null


            //根据加载状态设置页码
            if (status == INIT) {
                //Activity重启 直接使用原有数据渲染
                value?.let { return@launchMain }
                //刷新状态 重置页码
            } else if (status == REFRESH) {
                page = limit
                //加载更多状态 增加页码
            } else if (status == LOAD_MORE) {
                page++
                //非重试状态 value不为空则为view重构 直接使用原数据
            } else if (status != RELOAD && value != null) {
                return@launchMain
            }

            //首次加载缓存数据
            if (firstCache) {
                firstCache = false
                response = cacheBlock()?.also {
                    value = it
                }
            }

            //网络数据设置
            response = networkBlock(page).also {
                if (response != it) {
                    value = it
                }
            }

            //首页将网络数据设置缓存
            if (page == limit) {
                response?.run {
                    cacheSaveBlock(this)
                }
            }
        }
    }

}

//分页数据合并
fun <T> PageLiveData<*>.applyData(page: Int, limit: Int, oldData: ArrayList<T>?, newData: ArrayList<T>) {
    oldData ?: return
    if (page != limit) newData.addAll(0,oldData)
}