package com.lee.library.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)//元注解，作用在注解之上
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {
    //setOnXXXListener
    String listenerSetter();

    //监听对象 View.OnXXXListener
    Class<?> listenerType();

    //onXXXClick(View v)
    String callBackListener();
}
