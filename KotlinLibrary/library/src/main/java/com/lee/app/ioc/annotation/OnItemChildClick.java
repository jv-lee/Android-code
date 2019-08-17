package com.lee.app.ioc.annotation;

import com.lee.app.adapter.LeeViewAdapter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnItemChildClickListener",listenerType = LeeViewAdapter.OnItemChildView.class,callBackListener = "onItemChild")
public @interface OnItemChildClick {
    String value();
}
