package com.lee.calendar

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import org.junit.Test

/**
 * @author jv.lee
 * @date 2020/11/17
 * @description
 */
class CacheTest {
    @Test
    fun test(){
        //实现 内存缓存、本地缓存、网络加载  三层数据

        //实现 内存缓存、本地缓存、网络加载  三层数据
        val datas = arrayOf("memory", "disk", "network")
        val memoryObservable =
            Observable.create { emitter: ObservableEmitter<String?> ->
                println(1)
                val data = datas[0]
                emitter.onNext(data)
                emitter.onComplete()
            }

        val diskObservable =
            Observable.create { emitter: ObservableEmitter<String?> ->
                println(2)
                val data = datas[1]
                emitter.onNext(data)
                emitter.onComplete()
            }

        val networkObservable =
            Observable.create(
                ObservableOnSubscribe { emitter: ObservableEmitter<String?> ->
                    println(3)
                    val data = datas[2]
                    emitter.onNext(data)
                    emitter.onComplete()
                } as ObservableOnSubscribe<String?>
            )

        //从 内存 磁盘 网络获取数据 按顺序执行 第一个不为空时就不再需要后面的数据 直接返回

        //从 内存 磁盘 网络获取数据 按顺序执行 第一个不为空时就不再需要后面的数据 直接返回
        val obj = Observable.concat(
            memoryObservable,
            diskObservable,
            networkObservable
        )
            .first("obj")
            .map {
                println(it)
                it
            }
            .subscribe({ s ->
                println("三级缓存中获取到的数据：$s")
            },{
                println(it)
            })
    }
}