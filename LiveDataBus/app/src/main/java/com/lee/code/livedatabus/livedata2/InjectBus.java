package com.lee.code.livedatabus.livedata2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})//用于参数成员变量上
@Retention(RetentionPolicy.RUNTIME)//JVM运行时 通过反射获取注解的值
public @interface InjectBus {
    String value();
}
