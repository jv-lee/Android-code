package com.lee.library.ioc.annotation;

import com.lee.library.refresh.LoadingMoreCallBack;

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
@EventBase(listenerSetter = "setLoadingMoreCallBack",listenerType = LoadingMoreCallBack.class,callBackListener = "onLoadingMore")
public @interface OnLoadingMore {
    int[] values();
}
