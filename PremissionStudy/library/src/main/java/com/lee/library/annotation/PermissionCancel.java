package com.lee.library.annotation;

import com.lee.library.PermissionActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jv.lee
 * @date 2019-10-19
 * @description 权限被取消注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionCancel {
    int requestCoe() default PermissionActivity.PARAM_REQUEST_CODE_DEFAULT;
}
