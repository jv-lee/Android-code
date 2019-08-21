package com.lee.db.subdatabase;

import android.database.sqlite.SQLiteDatabase;

import com.lee.db.database.BaseDao;
import com.lee.db.database.BaseDaoFactory;

/**
 * @author jv.lee
 * @date 2019-08-21
 * @description
 */
public class BaseDaoSubFactory extends BaseDaoFactory {

    private static BaseDaoSubFactory instance;

    public static BaseDaoSubFactory getInstance() {
        synchronized (BaseDaoSubFactory.class) {
            if (instance == null) {
                instance = new BaseDaoSubFactory();
            }
        }
        return instance;
    }

    private BaseDaoSubFactory() {
    }

    /**
     * 定义一个用于实现分库的数据库对象
     */
    protected SQLiteDatabase sqLiteDatabase;

    /**
     * 获取自定义dao
     *
     * @param daoClass    dao类类型
     * @param entityClass 数据类类型
     * @param <T>         dao泛形
     * @param <M>         数据泛形
     * @return
     */
    @Override
    public <T extends BaseDao<M>, M> T getBaseDao(Class<T> daoClass, Class<M> entityClass) {
        BaseDao baseDao = null;
        if (daoMap.get(PrivateDatabaseEnums.database.getValue()) != null) {
            return (T) daoMap.get(PrivateDatabaseEnums.database.getValue());
        }
        //私有数据库创建
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(PrivateDatabaseEnums.database.getValue(), null);
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(sqLiteDatabase, entityClass);
            //添加缓存
            daoMap.put(PrivateDatabaseEnums.database.getValue(), baseDao);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }

}
