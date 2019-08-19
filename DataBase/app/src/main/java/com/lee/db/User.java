package com.lee.db;

import com.lee.db.annotation.DBField;
import com.lee.db.annotation.DBTable;

/**
 * @author jv.lee
 * @date 2019-08-19
 * @description
 */
@DBTable("tb_user")
public class User {
    @DBField("u_id")
    private int id;
    private String name;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
