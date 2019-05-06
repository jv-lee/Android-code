package com.lee.library.ioc.annotation;

import com.lee.library.refresh.RefreshCallBack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jv.lee
 * @date 2019/3/31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setRefreshCallBack",listenerType = RefreshCallBack.class,callBackListener = "onRefresh")
public @interface OnRefresh {
    int[] values();
}
