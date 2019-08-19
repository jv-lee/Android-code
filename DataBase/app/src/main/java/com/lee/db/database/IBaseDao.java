package com.lee.db.database;

/**
 * @author jv.lee
 * @date 2019-08-19
 * @description
 */
public interface IBaseDao<T> {
    long insert(T entity);
}
