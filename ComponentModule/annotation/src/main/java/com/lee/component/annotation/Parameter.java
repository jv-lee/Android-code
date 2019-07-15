package com.lee.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jv.lee
 * @date 2019/7/15.
 * @description
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Parameter {
    //不填写name的注解值表示该属性名就是key，填写了就用注解值作为key，从getIntent()方法中获取传递的参数
    String name() default "";
}
