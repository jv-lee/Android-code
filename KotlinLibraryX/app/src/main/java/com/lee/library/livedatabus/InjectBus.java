package com.lee.library.livedatabus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})//用于参数成员变量上
@Retention(RetentionPolicy.RUNTIME)//JVM运行时 通过反射获取注解的值
public @interface InjectBus {
    String value();

    /**
     * 当前为页面激活状态才接收消息
     *
     * @return
     */
    boolean isActive() default true;

    /**
     * 当前是否支持延时粘性消息
     * 暂时只支持最后一条消息接收， 不支持队列
     *
     * @return
     */
    boolean isViscosity() default false;
}
