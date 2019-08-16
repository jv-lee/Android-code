package com.gionee.gnservice.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author Created by caocong on 10/28/16.
 */
public class MemberPrivilege implements Serializable {
    private static final long serialVersionUID = 1852562467753980848L;
    private int id;
    private String name;
    private String description;
    private String iconUrl;
    private String icon2Url;
    private String imgUrl;
    private List<MemberPrivilegeContent> contentParts;
    private MemberLevel memberLevel;

    public MemberPrivilege() {

    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIcon2Url() {
        return icon2Url;
    }

    public void setIcon2Url(String icon2Url) {
        this.icon2Url = icon2Url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<MemberPrivilegeContent> getContentParts() {
        return contentParts;
    }

    public void setContentParts(List<MemberPrivilegeContent> contentParts) {
        this.contentParts = contentParts;
    }

    public MemberLevel getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(MemberLevel memberLevel) {
        this.memberLevel = memberLevel;
    }

    @Override
    public String toString() {
        return "MemberPrivilege{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", icon2Url='" + icon2Url + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
