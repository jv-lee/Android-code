package com.lee.db.entity;

import com.lee.db.annotation.DBTable;

/**
 * @author jv.lee
 * @date 2019-08-21
 * @description
 */
@DBTable("tb_photo")
public class Photo {

    private String time;
    private String path;

    public Photo(){
    }

    public Photo(String time, String path) {
        this.time = time;
        this.path = path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
