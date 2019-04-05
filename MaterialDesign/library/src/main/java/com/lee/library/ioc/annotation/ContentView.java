package com.lee.library.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)//该注解作用在 类 上
@Retention(RetentionPolicy.RUNTIME)//JVM运行时 通过反射获取注解的值
public @interface ContentView {
    int value();
}
