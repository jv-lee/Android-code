package com.lee.db.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author jv.lee
 * @date 2019-08-19
 * @description
 */
public class BaseDaoFactory<T> {

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

    public BaseDao<T> getBaseDao(Class<T> entityClass) {
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
}
