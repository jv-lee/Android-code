package com.lee.db.dao;


import com.lee.db.database.BaseDao;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019-08-21
 * @description 自定义自己的需求
 */
public class SqlDao<T> extends BaseDao<T> {

    public List<T> query(String sql) {
        return null;
    }

}
