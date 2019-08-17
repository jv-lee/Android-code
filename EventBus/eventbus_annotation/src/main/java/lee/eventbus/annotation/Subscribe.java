package lee.eventbus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lee.eventbus.annotation.mode.ThreadMode;

/**
 * @author jv.lee
 * @date 2019-08-17
 * @description
 */
@Target(ElementType.METHOD)//注解作用于方法之上
@Retention(RetentionPolicy.CLASS)//要在编译时进行一些预处理操作，注解回在class文件中存在
public @interface Subscribe {

    /**
     * 默认推荐POSTING（订阅、发布在同一线程）
     *
     * @return 注解线程模式
     */
    ThreadMode threadMode() default ThreadMode.POSTING;

    /**
     * @return 是否使用粘性事件
     */
    boolean sticky() default false;

    /**
     * 事件订阅优先级，在同一个线程中，数值越大优先级越高
     *
     * @return number 0 < n
     */
    int priority() default 0;
}
