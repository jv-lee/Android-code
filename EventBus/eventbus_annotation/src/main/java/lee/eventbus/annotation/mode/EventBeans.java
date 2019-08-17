package lee.eventbus.annotation.mode;

/**
 * @author jv.lee
 * @date 2019-08-17
 * @description 订阅自定义事件bean 获取订阅类 及 通知方法数据
 */
public class EventBeans implements SubscriberInfo {

    /**
     * 订阅者对象class
     */
    private final Class subscriberClass;

    /**
     * 订阅者方法数组
     */
    private final SubscriberMethod[] subscriberMethods;

    public EventBeans(Class subscriberClass, SubscriberMethod[] subscriberMethods) {
        this.subscriberClass = subscriberClass;
        this.subscriberMethods = subscriberMethods;
    }

    @Override
    public Class<?> getSubscriberClass() {
        return subscriberClass;
    }

    @Override
    public SubscriberMethod[] getSubscriberMethods() {
        return subscriberMethods;
    }
}
