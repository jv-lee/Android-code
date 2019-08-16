package com.gionee.gnservice.entity;

import java.io.Serializable;

/**
 * Created by caocong on 10/28/16.
 */
public class MemberPrivilegeContent implements Serializable {
    private int id;
    private int cid;
    private String name;
    private String content;

    public MemberPrivilegeContent() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MemberPrivilegeContent{" +
                "id=" + id +
                ", cid=" + cid +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
