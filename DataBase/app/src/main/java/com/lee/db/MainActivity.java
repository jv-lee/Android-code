package com.lee.db;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.db.database.BaseDao;
import com.lee.db.database.BaseDaoFactory;
import com.lee.db.database.IBaseDao;

import java.util.List;

/**
 * @author jv.lee
 * 数据库框架编写几大要点：
 * 1、如何自动创建数据库
 * 2、如何自动创建数据表
 * 3、如何让用户在使用的时候非常方便
 * 4、将user对象里面的类名 属性 转换成 创建数据库表的sql语句
 * create table user（id integer，name text，password text）
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private IBaseDao<User> dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = BaseDaoFactory.getInstance().getBaseDao(User.class);

        findViewById(R.id.btn_insert).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_update).setOnClickListener(this);
        findViewById(R.id.btn_select).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                insert();
                break;
            case R.id.btn_delete:
                delete();
                break;
            case R.id.btn_update:
                update();
                break;
            case R.id.btn_select:
                select();
                break;
            default:
        }
    }

    private void insert() {
        for (int i = 0; i < 10; i++) {
            long insert = dao.insert(new User(i, "username" + i, "123456"));
            Log.i(TAG, "insert: " + insert);
        }
    }

    private void delete() {
        User where = new User();
        where.setName("jv.lee");
        long delete = dao.delete(where);
        Toast.makeText(this, "删除成功：" + delete, Toast.LENGTH_SHORT).show();
    }

    private void update() {
        User user = new User();
        user.setId(2);
        user.setName("jv.lee");
        user.setPassword("123456");

        User where = new User();
        where.setId(2);
        long update = dao.update(user, where);
        Toast.makeText(this, "修改成功：" + update, Toast.LENGTH_SHORT).show();
    }

    private void select() {
        User where = new User();
        where.setPassword("123456");
        List<User> query = dao.query(where);
        for (User user : query) {
            Log.i(TAG, "select: " + user.toString());
        }
    }
}
