package com.lee.db.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.lee.db.annotation.DBField;
import com.lee.db.annotation.DBTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author jv.lee
 * @date 2019-08-19
 * @description
 */
public class BaseDao<T> implements IBaseDao<T> {

    private static final String TAG = "BaseDao";

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
    public boolean init(SQLiteDatabase sqLiteDatabase, Class<T> entityClass) {
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
                            .append(" TEXT");
                } else if (type == Integer.class || type == int.class) {
                    stringBuilder.append(dbField.value())
                            .append(" INTEGER");
                } else if (type == Long.class || type == long.class) {
                    stringBuilder.append(dbField.value())
                            .append(" BIGINT");
                } else if (type == Double.class || type == double.class) {
                    stringBuilder.append(dbField.value())
                            .append(" DOUBLE");
                } else if (type == byte[].class) {
                    stringBuilder.append(dbField.value())
                            .append(" BLOB");
                } else {
                    //不支持的类型
                    try {
                        throw new ClassNotFoundException("数据库不支持该类型");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (dbField.primarykey()) {
                    stringBuilder.append(" PRIMARY KEY,");
                } else {
                    stringBuilder.append(",");
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

    @Override
    public long delete(T where) {
        Map<String, String> values = getValues(where);
        Condition condition = new Condition(values);
        return sqLiteDatabase.delete(tableName, condition.whereCause, condition.whereArgs);
    }

    @Override
    public long update(T entity, T where) {
        //获取查询条件
        ContentValues contentValues = getContentValues(getValues(entity));
        Condition condition = new Condition(getValues(where));
        return sqLiteDatabase.update(tableName, contentValues, condition.whereCause, condition.whereArgs);
    }

    @Override
    public List<T> query(T where) {
        return query(where, null, null, null);
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        Map map = getValues(where);
        String limitString = null;
        if (startIndex != null && limit != null) {
            limitString = startIndex + " , " + limit;
        }
        Condition condition = new Condition(map);
        Cursor cursor = sqLiteDatabase.query(tableName, null, condition.whereCause, condition.whereArgs, null, null, orderBy, limitString);

        //定义一个解析游标的方法
        List<T> result = getCursorResult(cursor, where);
        cursor.close();
        return result;
    }

    /**
     * 获取游标中数据
     *
     * @param cursor 需要解析的游标
     * @param object 游标中的数据创建类型
     * @return 游标中数据的list集合
     */
    private List<T> getCursorResult(Cursor cursor, T object) {
        ArrayList list = new ArrayList();
        Object item = null;

        while (cursor.moveToNext()) {
            try {
                //通过反射创建对象 实例化 = new T
                item = object.getClass().newInstance();
                Iterator iterator = cacheMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    //获取列名
                    String columnName = (String) entry.getKey();
                    //以列名拿到在游标中的位置
                    Integer columnIndex = cursor.getColumnIndex(columnName);
                    //获取成员变量的类型 最终通过反射将对象的值set进去
                    Field field = (Field) entry.getValue();
                    Class type = field.getType();
                    if (columnIndex != -1) {
                        if (type == String.class) {
                            field.set(item, cursor.getString(columnIndex));
                        } else if (type == Double.class) {
                            field.set(item, cursor.getDouble(columnIndex));
                        } else if (type == Integer.class) {
                            field.set(item, cursor.getInt(columnIndex));
                        } else if (type == Long.class) {
                            field.set(item, cursor.getLong(columnIndex));
                        } else if (type == byte[].class) {
                            field.set(item, cursor.getBlob(columnIndex));
                        }
                    }
                }
                list.add(item);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Log.i(TAG, "getCursorResult: 实体类 没有public修饰的构造函数");
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    /**
     * 帮助构建 查询条件及条件value 类
     */
    private class Condition {
        /**
         * 占位符 查询条件字符串  where 1=1 and id=? and name=?
         */
        private String whereCause;
        /**
         * 占位符？ 的实际值
         */
        private String[] whereArgs;

        public Condition(Map<String, String> whereMap) {
            ArrayList list = new ArrayList();
            StringBuilder builder = new StringBuilder();
            builder.append("1=1");
            //获取所有的字段名
            Set keys = whereMap.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String vaule = whereMap.get(key);
                if (vaule != null) {
                    //拼接查询条件
                    builder.append(" and ").append(key).append(" =?");
                    //存储占位符实际value
                    list.add(vaule);
                }
            }
            this.whereCause = builder.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
        }

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
