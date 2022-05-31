package com.lee.library.db.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * 通用数据库方法BaseDao类
 * @author jv.lee
 * @date 2020/4/16
 */
interface BaseDao<T> {

    /**
     * vararg 可变长度参数 等同于java的 params...
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg value: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<T>)

    @Delete
    fun delete(vararg value: T)

    @Delete
    fun delete(list: List<T>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg value: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(list: List<T>)
}