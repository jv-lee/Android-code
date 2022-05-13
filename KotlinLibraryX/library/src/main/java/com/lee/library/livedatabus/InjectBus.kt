package com.lee.library.livedatabus

/**
 * @author jv.lee
 * @date 2019/3/30
 * @description LiveDataBus 事件监听方法注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectBus(
    val value: String, // 事件key
    val isActive: Boolean = true, // 当前为页面激活状态才接收消息
    val isViscosity: Boolean = false // 当前是否支持延时粘性消息（暂时只支持最后一条消息接收，不支持队列）
)