package lee.eventbus.annotation.mode;

/**
 * @author jv.lee
 * @date 2019-08-17
 * @description 订阅接口
 */
public interface SubscriberInfo {

    /**
     * @return 订阅所属类
     */
    Class<?> getSubscriberClass();

    /**
     *
     * @return 获取订阅所属类中所有订阅事件的方法
     */
    SubscriberMethod[] getSubscriberMethods();
}
