package com.gionee.gnservice.module.main;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by caocong on 10/31/16.
 */
public class MemberAction implements Serializable {
    private static final long serialVersionUID = -2998054732530232717L;
    private int type;
    private String content;
    private String description;
    private String bitmapUrl;
    private transient Bitmap bitmap;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getBitmapUrl() {
        return bitmapUrl;
    }

    public void setBitmapUrl(String bitmapUrl) {
        this.bitmapUrl = bitmapUrl;
    }

    @Override
    public String toString() {
        return "MemberAction{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", bitmap=" + bitmap +
                '}';
    }
}
