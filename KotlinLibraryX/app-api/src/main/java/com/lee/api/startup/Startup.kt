package com.lee.api.startup

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import com.lee.library.cache.CacheManager
import java.util.*

object Startup {
    fun initialize(context: Context) {
        AppInitializer.getInstance(context).initializeComponent(SyncInitializer::class.java)
    }
}

/**
 * SyncInitializer 同步初始化需要在使用时手动调用Startup.initialize来进行初始化
 * meta-data 标签中需要加入 tools:node="remove" 这样就不会进行自动初始化了
 * <meta-data
 *  android:name="com.lee.api.startup.AutoInitializer"
 *  android:value="androidx.startup"
 *  tools:node="remove" />
 */
class SyncInitializer : Initializer<Startup> {
    override fun create(context: Context): Startup {
        // 此处初始化 sdk 或其他初始化任务
        return Startup
    }

    // 初始化依赖项
    override fun dependencies(): MutableList<Class<out Initializer<*>>> = Collections.emptyList()
}

/**
 * AutoInitializer 自动初始化 无效在其他位置调用
 * <meta-data
 *  android:name="com.lee.api.startup.AutoInitializer"
 *  android:value="androidx.startup" />
 */
class AutoInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        // 此处初始化sdk 或其他初始化任务
        CacheManager.init(context, 1)
    }

    // 初始化依赖项
    override fun dependencies(): MutableList<Class<out Initializer<*>>> = Collections.emptyList()
}