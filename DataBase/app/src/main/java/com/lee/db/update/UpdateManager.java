package com.lee.db.update;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lee.db.dao.UserDao;
import com.lee.db.database.BaseDaoFactory;
import com.lee.db.entity.User;

import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author jv.lee
 * @date 2019/8/26.
 * @description 数据库更新管理类
 */
public class UpdateManager {

    private static final String TAG = "UpdateManager";

    private List<User> userList;

    public void startUpdateDb(Context context) {
        UserDao userDao = BaseDaoFactory.getInstance().getBaseDao(UserDao.class, User.class);
        userList = userDao.query(new User());
        //解析xml文件
        UpdateXml updateXml = readDbXml(context);
        //拿到当前版本信息
        UpdateStep updateStep = analyseUpdateStep(updateXml);
        if (updateStep == null) {
            return;
        }
        //获取更新用的对象
        List<UpdateDb> updateDbs = updateStep.getUpdateDbs();
        for (User user : userList) {
            //得到每一个用户的数据库对象
            SQLiteDatabase database = getDb(user.getId());
            if (database == null) {
                return;
            }
            for (UpdateDb updateDb : updateDbs) {
                String sql_rename = updateDb.getSql_rename();
                String sql_create = updateDb.getSql_create();
                String sql_insert = updateDb.getSql_insert();
                String sql_delete = updateDb.getSql_delete();
                String[] sqls = new String[]{sql_rename, sql_create, sql_insert, sql_delete};
                executeSql(database, sqls);
                Log.i(TAG, "startUpdateDb: " + user.getId() + "用户数据库升级成功");
            }
        }
    }

    private void executeSql(SQLiteDatabase database, String[] sqls) {
        if (sqls == null || sqls.length == 0) {
            return;
        }
        //开启事务
        database.beginTransaction();
        for (String sql : sqls) {
            sql.replace("\r", " ");
            sql.replace("\n", " ");
            sql.replace("\r\n", " ");
            if (!"".equals(sql.trim())) {
                database.execSQL(sql);
            }
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    /**
     * 获取所有用户的数据库
     *
     * @param id
     * @return
     */
    private SQLiteDatabase getDb(Integer id) {
        SQLiteDatabase sqlDb = null;
        File file = new File("data/data/com.lee.db/u_" + id + "_private.db");
        if (file.exists()) {
            return SQLiteDatabase.openOrCreateDatabase(file, null);
        }
        Log.i(TAG, "getDb: 当前用户数据库不存在：" + file.getAbsolutePath());
        return null;
    }

    /**
     * 读取update xml根节点
     *
     * @param updateXml
     * @return
     */
    private UpdateStep analyseUpdateStep(UpdateXml updateXml) {
        UpdateStep updateStep = null;
        if (updateXml == null) {
            return null;
        }
        List<UpdateStep> steps = updateXml.getUpdateSteps();
        if (steps == null || steps.size() == 0) {
            return null;
        }
        for (UpdateStep step : steps) {
            if (step.getVersionFrom() != null && step.getVersionTo() != null) {
                String[] versionArray = step.getVersionFrom().split(",");
                if (versionArray != null && versionArray.length > 0) {
                    for (int i = 0; i < versionArray.length; i++) {
                        //V002通过 sp保存到本地 V002代表当前版本信息  , V003应该从服务器获取
                        if ("V002".equalsIgnoreCase(versionArray[i]) && step.getVersionTo().equalsIgnoreCase("V003")) {
                            updateStep = step;
                            break;
                        }
                    }
                }
            }
        }

        return updateStep;
    }

    /**
     * 读取更新xml配置文件
     *
     * @param context
     * @return
     */
    private UpdateXml readDbXml(Context context) {
        InputStream is = null;
        Document document = null;
        try {
            is = context.getAssets().open("updateXml.xml");
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (document == null) {
                    return null;
                }
            }
        }
        UpdateXml xml = new UpdateXml(document);
        return xml;
    }
}
