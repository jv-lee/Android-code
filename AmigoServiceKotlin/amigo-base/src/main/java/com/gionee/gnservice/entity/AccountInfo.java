package com.gionee.gnservice.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

public class AccountInfo implements Serializable {
    private static final long serialVersionUID = -2281771846970380096L;
    private String userName;
    private String userId;
    private transient Bitmap photo;
    private String token;
    private MemberLevel memberLevel;
    private String nickName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MemberLevel getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(MemberLevel memberLevel) {
        this.memberLevel = memberLevel;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", photo=" + photo +
                ", token='" + token + '\'' +
                ", memberLevel=" + memberLevel +
                '}';
    }

    public void copy(AccountInfo accountInfo) {
        userName = accountInfo.getUserName();
        photo = accountInfo.getPhoto();
        userId = accountInfo.getUserId();
        token = accountInfo.getToken();
        memberLevel = accountInfo.getMemberLevel();
        nickName = accountInfo.getNickName();
    }

    public boolean isSameMemberLevel(AccountInfo info) {
        boolean isSameLevel = false;
        try {
            isSameLevel = memberLevel.getValue() == info.getMemberLevel().getValue();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return isSameLevel;
    }
}
