package com.lee.calendar

import org.junit.Test

/**
 * @author jv.lee
 * @date 2021/1/13
 * @description
 */
class ObjectCreateTest {

    data class Obj(var id: Int = 0) : Cloneable {
        public override fun clone(): Obj {
            return super.clone() as Obj
        }
    }

    @Test
    fun testCreate() {
        val runtime = Runtime.getRuntime()
        val startMemory = runtime.freeMemory()
        val objs = arrayListOf<Obj>()
        val obj = Obj(1)

        for (index in 0..100000) {
//            objs.add(obj.copy(id = index))       //3522336
//            objs.add(Obj(index))               //3341320
            objs.add(obj.apply { id = index }) //1382568
//            objs.add(obj.clone().apply { id = index })
        }

        val endMemory = runtime.freeMemory()
        val currentMemory = startMemory - endMemory
        println("start:$startMemory - end:$endMemory - current:$currentMemory")
    }
}