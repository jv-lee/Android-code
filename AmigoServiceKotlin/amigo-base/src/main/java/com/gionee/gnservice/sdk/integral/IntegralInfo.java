package com.gionee.gnservice.sdk.integral;

import java.io.Serializable;

/**
 * Created by caocong on 5/4/17.
 */

public class IntegralInfo implements Serializable {
    private int type;
    private String content;
    private String name;
    private String description;
    private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
