package com.netease.core.network.rx.databus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jv.lee
 * @date 2019/10/22.
 * @description 数据总线
 */
public class RxBus {
    private final static String START_RUN = "doProcessInvoke start emitter run";

    private Set<Object> subscribers;

    /**
     * 注册
     */
    public synchronized void register(Object subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * 移除
     */
    public synchronized void unRegister(Object subscriber) {
        subscribers.remove(subscriber);
    }

    private static volatile RxBus instance;

    private RxBus() {
        //稳定的 安全的 Set集合
        subscribers = new CopyOnWriteArraySet<>();
    }

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    /**
     * 提供外界使用 网络耗时操作
     *
     * @param function
     */
    public void doProcessInvoke(Function function) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(START_RUN);
                emitter.onComplete();
            }
        })
                .map(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object data) throws Exception {
                        if (data != null) {
                            sendDataAction(data);
                        }
                    }
                });
    }

    private void sendDataAction(Object data) {
        //扫描注册进来的对象，所以需要遍历subscribers容器
        for (Object subscriber : subscribers) {
            checkSubscriberAnnotationMethod(subscriber, data);
        }
    }

    private void checkSubscriberAnnotationMethod(Object subscriberTarget, Object data) {
        Method[] declaredMethods = subscriberTarget.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            method.setAccessible(true);

            RegisterRxBus annotation = method.getAnnotation(RegisterRxBus.class);
            if (annotation != null) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                String name = parameterTypes[0].getName();

                if (data.getClass().getName().equals(name)) {
                    try {
                        method.invoke(subscriberTarget, new Object[]{data});
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
