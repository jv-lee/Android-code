package com.lee.db.subdatabase;

import com.lee.db.entity.User;
import com.lee.db.database.BaseDaoFactory;
import com.lee.db.dao.UserDao;

import java.io.File;

/**
 * @author jv.lee
 * @date 2019-08-21
 * @description
 */
public enum PrivateDatabaseEnums {

    /**
     * 私有数据库
     */
    database("");

    private String value;

    PrivateDatabaseEnums(String value) {

    }

    public String getValue() {
        UserDao userDao = BaseDaoFactory.getInstance().getBaseDao(UserDao.class, User.class);
        if (userDao != null) {
            User currentUser = userDao.getCurrentUser();
            if (currentUser != null) {
                File file = new File("data/data/com.lee.db/");
                if (!file.exists()) {
                    file.mkdir();
                }
                return file.getAbsolutePath() + "/u_" + currentUser.getId() + "_private.db";
            }
        }
        return null;
    }

    }
