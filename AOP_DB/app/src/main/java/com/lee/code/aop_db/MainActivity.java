package com.lee.code.aop_db;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity implements DBOperation {

    private DBOperation db;
    private final String TAG = "DB-AOP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //代理db
        db = (DBOperation) Proxy.newProxyInstance(DBOperation.class.getClassLoader(),
                new Class[]{DBOperation.class}, new DBHandler(this));

        db.delete();
    }

    class DBHandler implements InvocationHandler {

        private DBOperation db;

        public DBHandler(DBOperation db) {
            this.db = db;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (db != null) {
                Log.i(TAG, "操作数据库之前，开始备份");
                db.save();
                Log.i(TAG, "数据备份完成，等待操作");
                return method.invoke(db, args);
            }
            return null;
        }
    }

    @Override
    public void insert() {
        Log.i(TAG, "新增数据");
    }

    @Override
    public void update() {
        Log.i(TAG, "修改数据");
    }

    @Override
    public void delete() {
        Log.i(TAG, "删除数据");
    }

    @Override
    public void save() {
        Log.i(TAG, "保存数据");
    }
}
