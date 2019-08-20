package com.lee.db.database;

import android.database.sqlite.SQLiteDatabase;

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

    private BaseDaoFactory() {
        String databasePath = "data/data/com.lee.db/app.db";
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
    }

    /**
     * 获取默认dao
     * @param entityClass
     * @return
     */
    public IBaseDao getBaseDao(Class entityClass) {
        BaseDao baseDao = null;
        try {
            baseDao = BaseDao.class.newInstance();
            baseDao.init(sqLiteDatabase, entityClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return baseDao;
    }

    /**
     * 获取自定义dao
     * @param daoClass dao类类型
     * @param entityClass 数据类类型
     * @param <T> dao泛形
     * @param <M> 数据泛形
     * @return
     */
    public <T extends BaseDao<M>, M> T getBaseDao(Class<T> daoClass, Class<M> entityClass) {
        BaseDao baseDao = null;
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(sqLiteDatabase, entityClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return (T)baseDao;
    }
}
