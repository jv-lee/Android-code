package com.lee.api.startup

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import java.util.*

/**
 * startup 初始化组件及依赖库
 * @author jv.lee
 * @date 2023/2/1
 */

object Startup {
    fun initialize(context: Context) {
        AppInitializer.getInstance(context).initializeComponent(SyncInitializer::class.java)
    }
}

class SyncInitializer : Initializer<Startup> {
    override fun create(context: Context): Startup {
        // 此处初始化 sdk 或其他初始化任务
        return Startup
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        // 初始化依赖项
        return Collections.emptyList()
    }
}