package com.lee.db;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lee.db.database.BaseDaoFactory;
import com.lee.db.database.IBaseDao;

/**
 * @author jv.lee
 * 数据库框架编写几大要点：
 * 1、如何自动创建数据库
 * 2、如何自动创建数据表
 * 3、如何让用户在使用的时候非常方便
 * 4、将user对象里面的类名 属性 转换成 创建数据库表的sql语句
 * create table user（id integer，name text，password text）
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IBaseDao<User> baseDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        baseDao.insert(new User());
    }
}
