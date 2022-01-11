package com.lee.api

import kotlin.reflect.KProperty

/**
 * @author jv.lee
 * @date 2022/1/11
 * @description
 */
class DelegateProperty {
    var name: String = "默认名字"

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