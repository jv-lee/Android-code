package lee.eventbus.core;

import lee.eventbus.annotation.mode.SubscriberMethod;

/**
 * @author jv.lee
 * @date 2019-08-18
 * @description 订阅事件封装类 ， 保存订阅者 类、订阅方法
 */
public class Subscription {
    /**
     * 订阅者 ： Activity.class
     */
    private final Object subscriber;

    /**
     * 订阅方法
     */
    private final SubscriberMethod subscriberMethod;

    Subscription(Object subscriber, SubscriberMethod subscriberMethod) {
        this.subscriber = subscriber;
        this.subscriberMethod = subscriberMethod;
    }

    @Override
    public boolean equals(Object other) {
        //必须重写方法，检测粘性事件重复调用（同一对象注册多个）
        if (other instanceof Subscription) {
            Subscription otherSubscription = (Subscription) other;
            //删除官方 subscriber == otherSubscription.subscriber判断条件
            //原因：粘性事件bug，多次调用和移除时重现，参考Subscription.java 37行
            return subscriberMethod.equals(otherSubscription.subscriberMethod);
        }
        return false;
    }

    public Object getSubscriber() {
        return subscriber;
    }

    public SubscriberMethod getSubscriberMethod() {
        return subscriberMethod;
    }
}
