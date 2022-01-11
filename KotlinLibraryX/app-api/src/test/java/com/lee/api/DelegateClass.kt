package com.lee.api

/**
 * @author jv.lee
 * @date 2022/1/11
 * @description kotlin 委托类
 */
interface Delegate {
    fun run()
    val type: String
}

class DelegateClass : Delegate {
    override fun run() {
        println("代理类执行run方法")
    }

    override val type: String
        get() = "委托类属性"

}

class DelegateImpl1 : Delegate by DelegateClass()

class DelegateImpl2(delegateClass: DelegateClass) : Delegate by delegateClass

class DelegateImpl3() : Delegate by DelegateClass() {
    override fun run() {
        println("自己执行实现的run方法")
    }

    override val type: String
        get() = "自己实现的type"
}