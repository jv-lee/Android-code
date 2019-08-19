package com.lee.library.ioc.annotation;

import com.lee.library.widget.StatusLayout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnReloadListener",listenerType = StatusLayout.OnReloadListener.class,callBackListener = "onReload")
public @interface OnReload {
    String value();
}
