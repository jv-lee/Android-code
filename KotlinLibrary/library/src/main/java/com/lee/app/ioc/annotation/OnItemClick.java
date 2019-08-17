package com.lee.app.ioc.annotation;

import com.lee.app.adapter.LeeViewAdapter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnItemClickListener",listenerType = LeeViewAdapter.OnItemClickListener.class,callBackListener = "onItemClick")
public @interface  OnItemClick {
    String value();
}
