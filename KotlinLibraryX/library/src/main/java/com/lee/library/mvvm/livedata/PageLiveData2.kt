package com.lee.library.mvvm.livedata

import com.lee.library.mvvm.base.BaseLiveData
import com.lee.library.mvvm.load.LoadStatus

/**
 * @author jv.lee
 * @date 2020/5/20
 * @description
 */
class PageLiveData2<T, K>(private val initKey: K? = null) : BaseLiveData<T>() {

    private var key: K? = null
    private var nextKey: K? = null
    private var firstCache = true

    suspend fun pageLaunch(
        @LoadStatus status: Int,
        startBlock: suspend () -> T? = { null },
        resumeBlock: suspend (K?) -> T? = { key: K? -> null },
        completedBlock: suspend (T) -> Unit = {}
    ) {
        try {
            var response: T? = null

            //根据加载状态设置页码
            //刷新状态 重置页码
            if(status == LoadStatus.INIT){
                //Activity重启 直接使用原有数据渲染
                value?.let { return }
            }else if (status == LoadStatus.REFRESH) {
                key = initKey
                //加载更多状态 增加页码
            } else if (status == LoadStatus.LOAD_MORE) {
                key = nextKey
                //非重试状态 value不为空则为view重构 直接使用原数据
            } else if (status != LoadStatus.RELOAD && value != null) {
                return
            }

            //首次加载缓存数据
            if (firstCache) {
                firstCache = false
                response = startBlock()?.also {
                    value = it
                }
            }

            //网络数据设置
            response = resumeBlock(key).also {
                if (response != it) {
                    value = it
                }
            }

            //首页将网络数据设置缓存
            if (key == initKey) {
                response?.run {
                    completedBlock(this)
                }
            }
        } catch (e: Exception) {
            throwMessage(e)
        }
    }

    fun putNextKey(nextKey: K) {
        this.nextKey = nextKey
    }

}