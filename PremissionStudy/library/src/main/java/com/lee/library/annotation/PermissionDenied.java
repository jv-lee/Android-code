package com.lee.library.annotation;

import com.lee.library.PermissionActivity;

/**
 * @author jv.lee
 * @date 2019-10-19
 * @description 权限被拒绝
 */
public @interface PermissionDenied {
    int requestCode() default PermissionActivity.PARAM_REQUEST_CODE_DEFAULT;
}
