package com.gionee.gnservice.entity;

import java.io.Serializable;

/**
 * Created by caocong on 5/31/17.
 */
public class IntegralTask implements Serializable {
    //任务类型
    public static final int PTYPE_TIRO = 1;
    public static final int PTYPE_DAILY = 2;

    //跳转类型
    public static final int TYPE_NONE = 0;
    public static final int TYPE_WEB = 1;
    public static final int TYPE_APP = 2;

    //完成状态
    public static final int STATUS_NOT_DONE = 0;//未完成
    public static final int STATUS_DONE = 1;//完成
    private int id;
    private String iconUrl;
    private int type;//跳转类型
    private String content;
    private String name;
    private String description;
    private int value;
    private int integralType; //任务类型
    private int status;//完成状态

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getIntegralType() {
        return integralType;
    }

    public void setIntegralType(int integralType) {
        this.integralType = integralType;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "IntegralTask{" +
                "id=" + id +
                ", iconUrl='" + iconUrl + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", value=" + value +
                ", integralType=" + integralType +
                ", status=" + status +
                '}';
    }
}
