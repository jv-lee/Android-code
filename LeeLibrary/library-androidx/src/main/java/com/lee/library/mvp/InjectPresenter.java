package com.lee.library.mvp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)//该注解作用在 成员变量 上
@Retention(RetentionPolicy.RUNTIME)//JVM运行时 通过反射获取注解的值
public @interface InjectPresenter {
    Class<?> value();
}
