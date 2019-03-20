package com.lee.library.ioc.annotation;

import com.lee.library.ioc.adapter.RView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnItemClickListener",listenerType = RView.OnItemClickListener.class,callBackListener = "onItemClick")
public @interface OnItemClick {
    int[] values();
}
