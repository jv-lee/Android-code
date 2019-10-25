package com.lee.permission.annotation;


import com.lee.permission.PermissionFragment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jv.lee
 * @date 2019-10-19
 * @description 申请权限注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    String[] value();

    int requestCode() default PermissionFragment.PARAM_REQUEST_CODE_DEFAULT;
}
