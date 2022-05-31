package com.lee.api

import kotlin.reflect.KProperty

/**
 * @author jv.lee
 * @date 2022/1/11

 */
class DelegateProperty {
    var name: String = "Delegate开发者"

    /**
     * @param thisRef 代表委托属性所属对象，因此thisDef类型需要于委托属性所属对象类型一致，或者是其父类
     * @param property 代表目标属性，该属性必须是KProperty<*>类型或其父类类型
     * @param value 为目标属性设置新的值
     */
    operator fun setValue(thisRef: SoftWareDev, property: KProperty<*>, value: String) {
        this.name = value
    }

    operator fun getValue(thisRef: SoftWareDev, property: KProperty<*>): String {
        return this.name
    }
}

interface SoftWareDev

class AndroidDev : SoftWareDev {
    var name: String by DelegateProperty()
}

class IosDev : SoftWareDev {
    var name: String by DelegateProperty()
}

class JavaDev : SoftWareDev

val JavaDev.name by DelegateProperty()

fun main(args: Array<String>) {
    val androidDev = AndroidDev()
    androidDev.name = "android开发"
    println(androidDev.name)

    val iosDev = IosDev()
    println(iosDev.name)

    println(JavaDev().name)
}