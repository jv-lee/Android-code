package lee.eventbus.annotation.mode;

import java.lang.reflect.Method;


/**
 * @author jv.lee
 * @date 2019-08-17
 * @description 订阅方法封装类  用于判断订阅方法 参数获取 ，及判断参数类型发送通知事件 ，及订阅类型 判断处理
 */
public class SubscriberMethod {

    /**
     * 订阅方法名
     */
    private String methodName;

    /**
     * 订阅方法，用于最后的自动执行订阅方法
     */
    private Method method;

    /**
     * 事件对象class 如：UserInfo
     */
    private Class<?> eventType;

    /**
     * 线程模式
     */
    private ThreadMode threadMode;

    /**
     * 事件订阅优先级（重新排序集合中的方法顺序）
     */
    private int priority;

    /**
     * 是否粘性事件（发送时存储，注册时判断粘性再激活）
     */
    private boolean sticky;

    public SubscriberMethod(Class subscriberClass, String methodName, Class<?> eventType, ThreadMode threadMode, int priority, boolean sticky) {
        this.methodName = methodName;
        this.eventType = eventType;
        this.threadMode = threadMode;
        this.priority = priority;
        this.sticky = sticky;

        try {
            //订阅所属类
            method = subscriberClass.getDeclaredMethod(methodName, eventType);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public Method getMethod() {
        return method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isSticky() {
        return sticky;
    }
}
