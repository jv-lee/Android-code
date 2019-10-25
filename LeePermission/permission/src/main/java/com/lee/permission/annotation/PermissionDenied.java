package com.lee.permission.annotation;


import com.lee.permission.PermissionFragment;

/**
 * @author jv.lee
 * @date 2019-10-19
 * @description 权限被拒绝
 */
public @interface PermissionDenied {
    int requestCode() default PermissionFragment.PARAM_REQUEST_CODE_DEFAULT;
}
