package lee.eventbus.annotation;

import lee.eventbus.annotation.mode.SubscriberInfo;

/**
 * @author jv.lee
 * @date 2019-08-17
 * @description 所有的事件订阅方法，生成索引接口
 */
public interface SubscriberInfoIndex {
    /**
     * 生成索引接口，通过订阅者对象 （MainActivity.class) 获取所有订阅方法
     * @param subscriberClass 订阅者class ： 如MainActivity
     * @return 事件订阅封装类
     */
    SubscriberInfo getSubscriberInfo(Class<?> subscriberClass);
}
