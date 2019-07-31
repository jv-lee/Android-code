package com.lee.webrtc.permission;

/**
 * @author jv.lee
 * @date 2019/4/14
 */
public interface PermissionRequest {

    /**
     * 请求权限成功
     */
    void onPermissionSuccess();

    /**
     * 请求权限失败
     * @param permission 权限字符
     */
    void onPermissionFiled(String permission);

}
