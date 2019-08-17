package lee.eventbus.compiler.utils;

/**
 * @author jv.lee
 * @date 2019-08-17
 * @description
 */
public class Constants {

    /**
     * 注解处理器中支持的注解类型
     * {@link lee.eventbus.annotation.Subscribe}
     */
    public static final String SUBSCRIBE_ANNOTATION_TYPES = "lee.eventbus.annotation.Subscribe";

    /**
     * APT生成的类文件所属包名
     */
    public static final String PACKAGE_NAME = "packageName";

    /**
     * APT生成的类名
     */
    public static final String CLASS_NAME = "className";

    /**
     * 所有的事件订阅方法，生成索引接口
     */
    public static final String SUBSCRIBER_INFO_INDEX = "lee.eventbus.annotation.SubscriberInfoIndex";

    /**
     * 全局属性名
     */
    public static final String FIELD_NAME = "SUBSCRIBER_INDEX";

    /**
     * putIndex方法的参数对象名
     */
    public static final String PUT_INDEX_PARAMETER_NAME = "info";

    /**
     * 加入Map集合方法名
     */
    public static final String PUT_INDEX_METHOD_NAME = "putIndex";

    /**
     * getSubscriberInfo方法的参数对象名
     */
    public static final String GET_SUBSCRIBER_INFO_PARAMETER_NAME = "subscriberClass";

    /**
     * 通过订阅者对象 （MainActivity.class）获取所有订阅方法的方法名
     */
    public static final String GET_SUBSCRIBER_INFO_METHOD_NAME = "getSubscriberInfo";
}
