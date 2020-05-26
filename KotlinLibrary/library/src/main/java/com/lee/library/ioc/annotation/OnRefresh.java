package com.lee.library.ioc.annotation;


import android.support.v4.widget.SwipeRefreshLayout;

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
@EventBase(listenerSetter = "setOnRefreshListener",listenerType = SwipeRefreshLayout.OnRefreshListener.class,callBackListener = "onRefresh")
public @interface OnRefresh {
    String value();
}
