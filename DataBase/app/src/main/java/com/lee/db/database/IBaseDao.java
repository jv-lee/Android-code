package com.lee.db.database;


import java.util.List;

/**
 * @author jv.lee
 * @date 2019-08-19
 * @description
 */
public interface IBaseDao<T> {

    long insert(T entity);

    long delete(T where);

    long update(T entity, T where);

    List<T> query(T where);

    List<T> query(T where, String orderBy, Integer startIndex, Integer limit);

}
