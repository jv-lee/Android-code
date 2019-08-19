package com.lee.db.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.lee.db.annotation.DBField;
import com.lee.db.annotation.DBTable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author jv.lee
 * @date 2019-08-19
 * @description
 */
public class BaseDao<T> implements IBaseDao<T> {

    /**
     * 持有数据库操作的引用
     */
    private SQLiteDatabase sqLiteDatabase;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 操作数据库对应的java类型
     */
    private Class<T> entityClass;

    /**
     * 是否已经做过初始化
     */
    private boolean isInit;

    /**
     * 字段缓存空间(key 字段名 value 成员变量）
     */
    private HashMap<String, Field> cacheMap;

    /**
     * 初始化dao表
     */
    protected boolean init(SQLiteDatabase sqLiteDatabase, Class<T> entityClass) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.entityClass = entityClass;

        if (!isInit) {
            //根据传入的Class进行数据库表创建 本例子中根据user对象创建
            DBTable annotation = entityClass.getAnnotation(DBTable.class);
            if (annotation != null && !"".equals(annotation.value())) {
                //获取表明值
                tableName = annotation.value();
            } else {
                //没有填写表名，反射获取类名
                tableName = entityClass.getName();
            }
            if (!sqLiteDatabase.isOpen()) {
                return false;
            }
            //创建表
            String createTableSql = getCreateTableSql();
            sqLiteDatabase.execSQL(createTableSql);
            cacheMap = new HashMap<>();
            initCacheMap();
            isInit = true;
        }
        return isInit;
    }

    private String getCreateTableSql() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("create table if not exists ")
                .append(tableName)
                .append("(");
        //反射得到所有的成员变量
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            Class type = field.getType();

            DBField dbField = field.getAnnotation(DBField.class);
            if (dbField != null && !"".equals(dbField.value())) {
                if (type == String.class) {
                    stringBuilder.append(dbField.value())
                            .append(" TEXT,");
                } else if (type == Integer.class) {
                    stringBuilder.append(dbField.value())
                            .append(" INTEGER,");
                } else if (type == Long.class) {
                    stringBuilder.append(dbField.value())
                            .append(" BIGINT,");
                } else if (type == Double.class) {
                    stringBuilder.append(dbField.value())
                            .append(" DOUBLE,");
                } else if (type == byte[].class) {
                    stringBuilder.append(dbField.value())
                            .append(" BLOB,");
                } else {
                    //不支持的类型
                    try {
                        throw new ClassNotFoundException("数据库不支持该类型");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (type == String.class) {
                    stringBuilder.append(field.getName())
                            .append(" TEXT,");
                } else if (type == Integer.class) {
                    stringBuilder.append(field.getName())
                            .append(" INTEGER,");
                } else if (type == Long.class) {
                    stringBuilder.append(field.getName())
                            .append(" BIGINT,");
                } else if (type == Double.class) {
                    stringBuilder.append(field.getName())
                            .append(" DOUBLE,");
                } else if (type == byte[].class) {
                    stringBuilder.append(field.getName())
                            .append(" BLOB,");
                } else {
                    //不支持的类型
                    try {
                        throw new ClassNotFoundException("数据库不支持该类型");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    /**
     * 初始化字段map空间
     */
    private void initCacheMap() {
        //取得所有的列名
        String sql = "select * from " + tableName + " limit 1,0";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();

        //获取所有的成员变量
        Field[] columnFields = entityClass.getDeclaredFields();
        //将字段访问权限打开
        for (Field columnField : columnFields) {
            columnField.setAccessible(true);
        }

        for (String columnName : columnNames) {
            Field columnField = null;
            for (Field field : columnFields) {
                String fieldName = null;
                if (field.getAnnotation(DBField.class) != null) {
                    fieldName = field.getAnnotation(DBField.class).value();
                } else {
                    fieldName = field.getName();
                }
                if (columnName.equals(fieldName)) {
                    columnField = field;
                    break;
                }
            }
            if (columnField != null) {
                cacheMap.put(columnName, columnField);
            }
        }
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public long insert(T entity) {
        // user 对象 转换为 content  Value
        Map<String, String> map = getValues(entity);
        ContentValues values = getContentValues(map);
        return sqLiteDatabase.insert(tableName, null, values);
    }

    /**
     * 将user对象的值转换为map对象
     *
     * @param entity
     * @return
     */
    private Map<String, String> getValues(T entity) {
        HashMap<String, String> map = new HashMap<>();
        //得到所有的成员变量,user的成员变量
        Iterator<Field> iterator = cacheMap.values().iterator();
        while (iterator.hasNext()) {
            Field field = iterator.next();
            field.setAccessible(true);
            //获取成员变量的值
            try {
                Object object = field.get(entity);
                if (object == null) {
                    continue;
                }
                String value = object.toString();
                //获取列名
                String key = null;

                DBField dbField = field.getAnnotation(DBField.class);
                if (dbField != null && !"".equals(dbField.value())) {
                    key = dbField.value();
                } else {
                    key = field.getName();
                }

                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    map.put(key, value);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 将map对象转换为 contentValues
     *
     * @return
     */
    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key, value);
            }
        }
        return contentValues;
    }
}
