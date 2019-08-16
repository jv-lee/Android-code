package com.gionee.gnservice.entity;

public class ServiceInfo {
    //会员类型
    private int memberType;
    private String name;
    private int icon;
    private String target;
    private String order;
    private boolean isApk;
    private String targetPackage;
    private String token;
    private boolean isAction;
    private boolean isNeedTnLogin = false;
    private boolean needShowTrafficAlertFirst = false;
    private boolean needBigDisplay = false;

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isApk() {
        return isApk;
    }

    public void setApk(boolean isApk) {
        this.isApk = isApk;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public boolean isAction() {
        return isAction;
    }

    public void setAction(boolean isAction) {
        this.isAction = isAction;
    }

    public boolean isNeedTnLogin() {
        return isNeedTnLogin;
    }

    public void setNeedTnLogin(boolean isNeedTnLogin) {
        this.isNeedTnLogin = isNeedTnLogin;
    }

    public boolean isNeedShowTrafficAlertFirst() {
        return needShowTrafficAlertFirst;
    }

    public void setNeedShowTrafficAlertFirst(boolean needShowTrafficAlertFirst) {
        this.needShowTrafficAlertFirst = needShowTrafficAlertFirst;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setBigDisplay(boolean big) {
        this.needBigDisplay = big;
    }

    public boolean needBigDisplay() {
        return needBigDisplay;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "ServiceInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", target='" + target + '\'' +
                ", isApk=" + isApk +
                ", targetPackage='" + targetPackage + '\'' +
                ", token='" + token + '\'' +
                ", isAction=" + isAction +
                ", isNeedTnLogin=" + isNeedTnLogin +
                ", needShowTrafficAlertFirst=" + needShowTrafficAlertFirst +
                ", needBigDisplay=" + needBigDisplay +
                '}';
    }
}
