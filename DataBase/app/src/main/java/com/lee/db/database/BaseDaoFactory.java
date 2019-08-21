package com.lee.db.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jv.lee
 * @date 2019-08-19
 * @description
 */
public class BaseDaoFactory {

    private static BaseDaoFactory instance;

    public static BaseDaoFactory getInstance() {
        synchronized (BaseDao.class) {
            if (instance == null) {
                instance = new BaseDaoFactory();
            }
        }
        return instance;
    }

    private SQLiteDatabase sqLiteDatabase;

    /**
     * 设计数据库连接池，new 容器，只需要new一个，下次就不会再创建。并且考虑多线程情况
     */
    protected Map<String, BaseDao> daoMap = Collections.synchronizedMap(new HashMap<String, BaseDao>());

    protected BaseDaoFactory() {
        String databasePath = "data/data/com.lee.db/app.db";
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
    }

    /**
     * 获取自定义dao
     *
     * @param daoClass    dao类类型
     * @param entityClass 数据类类型
     * @param <T>         dao泛形
     * @param <M>         数据泛形
     * @return
     */
    public <T extends BaseDao<M>, M> T getBaseDao(Class<T> daoClass, Class<M> entityClass) {
        BaseDao baseDao = null;
        if (daoMap.get(daoClass.getSimpleName()) != null) {
            return (T) daoMap.get(daoClass.getSimpleName());
        }
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(sqLiteDatabase, entityClass);
            //添加缓存
            daoMap.put(daoClass.getSimpleName(), baseDao);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }
}
