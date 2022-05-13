package com.lee.library.livedatabus

/**
 * @author jv.lee
 * @date 2019/3/30
 * @description LiveDataBus 事件监听方法注解
 * @param isActive 当前为页面激活状态才接收消息
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectBus(val isActive: Boolean = true)