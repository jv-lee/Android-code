package com.lee.library.adapter.manager

import com.lee.library.adapter.listener.DefaultLoadResource
import com.lee.library.adapter.listener.LoadResource

/**
 * @author jv.lee
 * @date 2020/8/11
 * @description 全局设置LoadResource资源
 */
class ViewLoadManager {

    companion object {

        @Volatile
        private var instance: ViewLoadManager? = null

        @JvmStatic
        fun getInstance() = instance
            ?: synchronized(this) {
            instance
                ?: ViewLoadManager()
                    .also { instance = it }
        }
    }

    private var loadResource: LoadResource? = null

    fun getLoadResource() = loadResource ?: DefaultLoadResource()

    fun setLoadResource(loadResource: LoadResource) {
        this.loadResource = loadResource
    }

}