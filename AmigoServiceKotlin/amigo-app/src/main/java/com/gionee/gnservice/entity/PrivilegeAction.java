package com.gionee.gnservice.entity;

import java.io.Serializable;

/**
 * Created by caocong on 7/8/17.
 */
public class PrivilegeAction implements Serializable {
    public static final int TYPE_WEB = 1;
    public static final int TYPE_START_APP = 2;

    private String id;
    private String name;
    private int type;
    private String imageUrl;
    private String content;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
        this.description = this.description;
    }

    @Override
    public String toString() {
        return "PrivilegeAction{" +
                "id = " + id +
                "name = " + name +
                "type=" + type +
                ", imageUrl='" + imageUrl + '\'' +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
