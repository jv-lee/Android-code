package com.lee.library;

/**
 * @author jv.lee
 * @date 2019/7/10.
 * @description 跳转类
 * path : "order/Order_MainActivity"
 * clazz : "Order_MainActivity.class"
 */
public class PathBean {
    private String path;
    private Class clazz;

    public PathBean() {
    }

    public PathBean(String path, Class clazz) {
        this.path = path;
        this.clazz = clazz;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
