package lee.eventbus.core;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lee.eventbus.annotation.SubscriberInfoIndex;
import lee.eventbus.annotation.mode.SubscriberInfo;
import lee.eventbus.annotation.mode.SubscriberMethod;

/**
 * @author jv.lee
 * @date 2019-08-18
 * @description ArrayList的底层是数组，查询和修改直接根据索引可以很快找到对应的元素（替换）
 * 而增加和删除就涉及到数组元素的移动，所以会比较慢
 * <p>
 * CopyOnWriteArrayList实现了List接口（读写分离）
 * Vector是增删改查方法都加了synchronized，保证同步，但是每个方法执行的时候都要取获得锁，性能就会大大下降
 * 而CopyOnWriteArrayList 只是在增删改上加锁，但是读取不加锁，在读取方面的性能就优于Vector
 * </p>
 * CopyOnWriteArrayList支持读多写少的并发情况
 */
public class EventBus {

    private static final String TAG = "EventBus";

    /**
     * volatile修饰符的变量不允许线程内部缓存和重新排序，即直接修改内存
     */
    private static volatile EventBus defaultInstance;

    /**
     * 索引接口
     */
    private SubscriberInfoIndex subscriberInfoIndex;

    /**
     * 订阅者类型集合，比如：订阅者Activity订阅了那些EventBean，或者接触订阅的缓存
     * key：订阅者Activity.class value：EventBean集合
     */
    private Map<Object, List<Class<?>>> typesBySubscriber;

    /**
     * 方法混存：key：订阅者Activity.class value:订阅者方法集合
     */
    private static final Map<Class<?>, List<SubscriberMethod>> METHOD_CACHE = new ConcurrentHashMap<>();

    /**
     * EventBean缓存，key：UserInfo.class value:订阅者（可以是多个Activity）中所有订阅的方法集合
     */
    private Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;

    /**
     * 粘性事件缓存 key:UserInfo.class(参数类型的类 类型) value:UserInfo (参数类型的 对象)
     */
    private final Map<Class<?>, Object> stickyEvents;

    /**
     * 发送（子线程） - 订阅 （主线程）
     */
    private Handler handler;

    /**
     * 发送（主线程） - 订阅（子线程）
     */
    private ExecutorService executorService;

    private EventBus() {
        // 初始化缓存集合
        typesBySubscriber = new HashMap<>();
        subscriptionsByEventType = new HashMap<>();
        stickyEvents = new HashMap<>();
        //Handler高级用法：将handler放在主线程使用
        handler = new Handler(Looper.getMainLooper());
        //创建一个子线程（缓存线程池）
        executorService = Executors.newCachedThreadPool();
    }

    public static EventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (EventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBus();
                }
            }
        }
        return defaultInstance;
    }

    /**
     * 添加索引（简化），接口= 接口实现类，参考EventBusBuilder.java 136行
     *
     * @param index 全局索引类 apt构建
     */
    public void addIndex(SubscriberInfoIndex index) {
        subscriberInfoIndex = index;
    }

    /**
     * 注册/订阅事件，参考EventBus.java 138行
     *
     * @param subscriber 订阅类
     */
    public void register(Object subscriber) {
        //获取订阅的Activity
        Class<?> subscriberClass = subscriber.getClass();
        //寻找 类的订阅方法集合
        List<SubscriberMethod> subscriberMethods = findSubscriberMethods(subscriberClass);
        synchronized (this) {
            for (SubscriberMethod subscriberMethod : subscriberMethods) {
                //遍历后开始订阅
                subscribe(subscriber, subscriberMethod);
            }
        }
    }

    /**
     * 寻找当前订阅类中的订阅方法集合，参考SubscriberMethodFinder.java 55行
     *
     * @param subscriberClass 订阅类
     * @return 订阅类的订阅方法集合
     */
    private List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {
        //从集合混存中读取
        List<SubscriberMethod> subscriberMethods = METHOD_CACHE.get(subscriberClass);
        if (subscriberMethods != null) {
            return subscriberMethods;
        }
        //没找到，从APT生成的类文件中寻找
        subscriberMethods = findUsingInfo(subscriberClass);
        if (subscriberMethods != null) {
            //保存至缓存中
            METHOD_CACHE.put(subscriberClass, subscriberMethods);
        }
        return subscriberMethods;
    }

    /**
     * 开始订阅事件，参考EventBus.java 149行
     *
     * @param subscriber       订阅类
     * @param subscriberMethod 订阅方法
     */
    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        //获取订阅方法参数类型，如：UserInfo.class
        Class<?> eventType = subscriberMethod.getEventType();
        //临时对象存储
        Subscription subscription = new Subscription(subscriber, subscriberMethod);
        //读取EventBean缓存
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions == null) {
            //初始化集合
            subscriptions = new CopyOnWriteArrayList<>();
            //存入缓存
            subscriptionsByEventType.put(eventType, subscriptions);
        } else {
            if (subscriptions.contains(subscription)) {
                Log.e(TAG, "subscribe: " + subscriber.getClass() + " 重复注册粘性事件！");
                //执行多次粘性事件，但不添加到缓存集合，避免订阅方法多次执行 执行完后直接return 避免重复添加
                sticky(subscriberMethod, eventType, subscription);
                return;
            }
        }

        //订阅方法优先级处理，第一次进来肯定是0，参考EventBus.java 163行
        int size = subscriptions.size();
        //这里 i<= size ，否则不满足条件
        for (int i = 0; i <= size; i++) {
            //如果满足任意条件则进入训啊还（第一次 i = size = 0）
            //第二次，size！=0 ，新加入的订阅方法匹配集合中所有订阅方法的优先级
            if (i == size || subscriberMethod.getPriority() > subscriptions.get(i).getSubscriberMethod().getPriority()) {
                //如果新加入的订阅方法优先级大于集合中某订阅方法优先级，则插队到它之前一位
                if (!subscriptions.contains(subscription)) {
                    subscriptions.add(i, subscription);
                    break;
                }
            }
        }

        //订阅者类集合，比如：订阅者Activity订阅了哪些EventBean，或者解除订阅的缓存
        List<Class<?>> subscribedEvents = typesBySubscriber.get(subscriber);
        if (subscribedEvents == null) {
            subscribedEvents = new ArrayList<>();
            //存入缓存
            typesBySubscriber.put(subscriber, subscribedEvents);
        }
        //注意：subscribe（）方法在遍历过程中，所以一直在添加
        subscribedEvents.add(eventType);

        //代码能走到此处为第一次粘性事件
        sticky(subscriberMethod, eventType, subscription);
    }

    /**
     * 抽取原因：可执行多次粘性事件，而不会出现闪退，参考EventBus.java 158行
     *
     * @param subscriberMethod 订阅方法实体
     * @param eventType        订阅事件返回类型 type
     * @param subscription     订阅事件封装类
     */
    private void sticky(SubscriberMethod subscriberMethod, Class<?> eventType, Subscription subscription) {
        //粘性事件触发：注册事件就激活方法，因为整个源码只有此处遍历了
        //最佳切入点原因：1.粘性事件的订阅方法加入了缓存。 2.注册时只有粘性事件直接激活方法（隔离非粘性事件）
        //新增开关方法弊端：粘性事件未缓存中，无法触发订阅者方法。且有可能多次执行post（）方法
        if (subscriberMethod.isSticky()) {
            //源码中做了继承关系处理，也说明了迭代效率和更改数据结构方便查找，此处省略
            Object stickyEvent = stickyEvents.get(eventType);
            //发送事件到订阅者的所有订阅方法，并激活方法
            if (stickyEvent != null) {
                postToSubscription(subscription, stickyEvent);
            }
        }
    }


    /**
     * 在APT生成的类文件中寻找订阅方法集合，参考SubscriberMethodFinder.java 64行
     *
     * @param subscriberClass 订阅类
     * @return 订阅类方法集合
     */
    private List<SubscriberMethod> findUsingInfo(Class<?> subscriberClass) {
        //app在运行时寻找索引，报错了则说明没有初始化索引方法
        if (subscriberInfoIndex == null) {
            throw new RuntimeException("未添加索引方法：EventBus.addIndex()");
        }
        //接口持有实现类的引用
        SubscriberInfo info = subscriberInfoIndex.getSubscriberInfo(subscriberClass);
        if (info != null) {
            //数组转List集合，参考EventBus生成的APT类文件
            return Arrays.asList(info.getSubscriberMethods());
        }
        return null;
    }

    /**
     * 是否 注册/订阅
     *
     * @param subscriber 目标类
     * @return 是否订阅
     */
    public synchronized boolean isRegistered(Object subscriber) {
        return typesBySubscriber.containsKey(subscriber);
    }

    /**
     * 解除订阅者关系 参考EventBus.java 239行
     *
     * @param subscriber 订阅者类
     */
    public synchronized void unregister(Object subscriber) {
        //从缓存中移除
        List<Class<?>> subscribedTypes = typesBySubscriber.get(subscriber);
        if (subscribedTypes != null) {
            //移除前清空集合
            subscribedTypes.clear();
            typesBySubscriber.remove(subscriber);
        }
    }

    /**
     * 发送粘性事件，最终还是调用了post方法，参考EventBus.java
     *
     * @param event 事件类型
     */
    public void postSticky(Object event) {
        //同步锁保证并发安全（小项目可以忽略此处）
        synchronized (stickyEvents) {
            //加入粘性事件缓存集合
            stickyEvents.put(event.getClass(), event);
        }
    }

    /**
     * 获取指定类型的粘性事件， 参考EventBus.java 314行
     *
     * @param eventType 粘性事件类型
     * @param <T>       对象类型
     * @return 粘性事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (stickyEvents) {
            //cast 方法做转换类型时安全措施
            return eventType.cast(stickyEvents.get(eventType));
        }
    }

    /**
     * 删除指定类型的粘性事件
     * @param eventType 事件数据对象类型
     * @param <T> 返回数据类型实体类
     * @return 事件数据类型
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        //同步锁保证并发安全
        synchronized (stickyEvents) {
            return eventType.cast(stickyEvents.remove(eventType));
        }
    }

    /**
     * 移除所有粘性事件，参考EventBus.java 352行
     */
    public void removeAllStickyEvents() {
        //同步锁保证并发安全（小项目可忽略此处）
        synchronized (stickyEvents) {
            //清理集合
            stickyEvents.clear();
        }
    }

    /**
     * 发送消息 / 事件
     *
     * @param event 事件类型
     */
    public void post(Object event) {
        //此处两个参数，简化了源码， 参考EventBus.java 252 - 265 - 384 - 400行
        postSingleEventForEventType(event, event.getClass());
    }

    /**
     * 为EventBean事件类型发布单个事件（遍历） ， EventBus核心：参数类型必须一致！！！
     *
     * @param event      事件参数类型
     * @param eventClass 事件订阅类
     */
    private void postSingleEventForEventType(Object event, Class<?> eventClass) {
        // 从EventBean缓存中，获取所有订阅者和订阅方法
        CopyOnWriteArrayList<Subscription> subscriptions;
        synchronized (this) {
            //同步锁，保证并发安全
            subscriptions = subscriptionsByEventType.get(eventClass);
        }
        //判空，健壮性代码
        if (subscriptions != null && !subscriptions.isEmpty()) {
            for (Subscription subscription : subscriptions) {
                //遍历，寻找发送方指定的EventBean，匹配的订阅方法的EventBean
                postToSubscription(subscription, event);
            }
        }
    }


    /**
     * 发送事件到订阅者的所有订阅方法（遍历中。。。） 参考参考EventBus.java 427行
     *
     * @param subscription 订阅事件封装类
     * @param event        粘性事件的数据类型 type
     */
    private void postToSubscription(final Subscription subscription, final Object event) {
        //匹配订阅方的线程模式
        switch (subscription.getSubscriberMethod().getThreadMode()) {
            //订阅、发布在同一线程
            case POSTING:
                invokeSubscriber(subscription, event);
                break;
            //订阅方是主线程 主线程发送事件到主线程
            case MAIN:
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    invokeSubscriber(subscription, event);
                } else {
                    //订阅方是子线程 子线程发送到主线程
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invokeSubscriber(subscription, event);
                        }
                    });
                }
                break;
            //订阅方是主线程，主线程发送到子线程
            case ASYNC:
                //订阅方是主线程 主线程发送到子线程
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    //主线程发送到子线程，创建一个子线程（缓存线程池）
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            invokeSubscriber(subscription, event);
                        }
                    });
                } else {
                    //订阅方是子线程，  子线程发送到子线程
                    invokeSubscriber(subscription, event);
                }
                break;
            default:
                throw new IllegalStateException("未知线程模式！" + subscription.getSubscriberMethod().getThreadMode());
        }
    }

    private void invokeSubscriber(Subscription subscription, Object event) {
        try {
            //无论是3.0之前还是之后，最后一步终究逃不过反射
            subscription.getSubscriberMethod().getMethod().invoke(subscription.getSubscriber(), event);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清理静态混存（视项目规模调用）
     */
    public static void clearCaChes() {
        METHOD_CACHE.clear();
    }

}


