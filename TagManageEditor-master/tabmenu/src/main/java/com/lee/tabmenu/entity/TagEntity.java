package com.lee.tabmenu.entity;

public class TagEntity {
    private long id;
    private String name;
    private int viewType;
    /**
     * 常驻标签
     */
    private boolean isResident;
    /**
     * 是否显示删除图标
     */
    private boolean isClose;
    /**
     * 是否显示添加图标
     */
    private boolean isAdd;

    public TagEntity() {
    }

    public TagEntity(long id, String name, int viewType, boolean isResident, boolean isClose, boolean isAdd) {
        this.id = id;
        this.name = name;
        this.viewType = viewType;
        this.isResident = isResident;
        this.isClose = isClose;
        this.isAdd = isAdd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean isResident() {
        return isResident;
    }

    public void setResident(boolean resident) {
        isResident = resident;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
