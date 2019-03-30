package com.lee.code.adapter.bean;

/**
 * @author jv.lee
 * @date 2019/3/29
 */
public class Userinfo {
    private String account;
    private String password;
    private int type;

    public Userinfo() {
    }

    public Userinfo(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
