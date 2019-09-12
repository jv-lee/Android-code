package com.lee.library.livedatabus;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jv.lee
 * @date 2019/3/30
 * 事件总线
 */
public class LiveDataBus {

    /**
     * 消息通道
     */
    private Map<String, BusMutableLiveData<Object>> bus;
    private static LiveDataBus instance;

    private LiveDataBus() {
        bus = new HashMap<>();
    }

    public static LiveDataBus getInstance() {
        if (instance == null) {
            synchronized (LiveDataBus.class) {
                if (instance == null) {
                    instance = new LiveDataBus();
                }
            }
        }
        return instance;
    }

    public <T> MutableLiveData<T> getChannel(String target, Class<T> type) {
        if (!bus.containsKey(target)) {
            bus.put(target, new BusMutableLiveData<>());
        }
        return (MutableLiveData<T>) bus.get(target);
    }

    public MutableLiveData<Object> getChannel(String target) {
        return getChannel(target, Object.class);
    }

    private static class ObserverWrapper<T> implements Observer<T> {

        private Observer<T> observer;

        public ObserverWrapper(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (observer != null) {
//                if (isCallOnObserve()) {
//                    return;
//                }
                observer.onChanged(t);
            }
        }

        private boolean isCallOnObserve() {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                for (StackTraceElement element : stackTrace) {
                    if ("android.arch.lifecycle.LiveData".equals(element.getClassName()) &&
                            "observeForever".equals(element.getMethodName())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private static class BusMutableLiveData<T> extends MutableLiveData<T> {

        private Map<Observer, Observer> observerMap = new HashMap<>();

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
            super.observe(owner, observer);
            try {
                hook(observer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void observeForever(@NonNull Observer<T> observer) {
            if (!observerMap.containsKey(observer)) {
                observerMap.put(observer, new ObserverWrapper(observer));
            }
            super.observeForever(observerMap.get(observer));
        }

        @Override
        public void removeObserver(@NonNull Observer<T> observer) {
            Observer realObserver = null;
            if (observerMap.containsKey(observer)) {
                realObserver = observerMap.remove(observer);
            } else {
                realObserver = observer;
            }
            super.removeObserver(realObserver);
        }

        private void hook(@NonNull Observer<T> observer) throws Exception {
            //get wrapper's version
            Class<LiveData> classLiveData = LiveData.class;
            Field fieldObservers = classLiveData.getDeclaredField("mObservers");
            fieldObservers.setAccessible(true);
            Object objectObservers = fieldObservers.get(this);
            Class<?> classObservers = objectObservers.getClass();
            Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
            methodGet.setAccessible(true);
            Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
            Object objectWrapper = null;
            if (objectWrapperEntry instanceof Map.Entry) {
                objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
            }
            if (objectWrapper == null) {
                throw new NullPointerException("Wrapper can not be bull!");
            }
            Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
            Field fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
            fieldLastVersion.setAccessible(true);
            //get livedata's version
            Field fieldVersion = classLiveData.getDeclaredField("mVersion");
            fieldVersion.setAccessible(true);
            Object objectVersion = fieldVersion.get(this);
            //set wrapper's version
            fieldLastVersion.set(objectWrapper, objectVersion);
        }
    }

    /**
     * 存储非激活事件的临时容器
     */
    private Map<String, Observer> tempMap = new HashMap<>();

    /**
     * 订阅通知
     * @param lifecycleOwner
     */
    public void injectBus(LifecycleOwner lifecycleOwner) {
        Class<? extends LifecycleOwner> aClass = lifecycleOwner.getClass();
        //获取当前类所有方法
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            //激活通知
            InjectBus injectBus = method.getAnnotation(InjectBus.class);
            if (injectBus != null) {
                String value = injectBus.value();
                boolean isActive = injectBus.isActive();

                Observer<Object> observer = o -> {
                    try {
                        method.invoke(lifecycleOwner, o);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                };

                //是否是激活状态通知
                if (isActive) {
                    LiveDataBus.getInstance().getChannel(value).observe(lifecycleOwner, observer);
                } else {
                    tempMap.put(value, observer);
                    LiveDataBus.getInstance().getChannel(value).observeForever(observer);
                }
            }

        }
    }

    /**
     * 取消订阅通知 (仅在使用非激活状态可通知模式 需要取消订阅)
     * @param lifecycleOwner
     */
    public void unInjectBus(LifecycleOwner lifecycleOwner) {
        Class<? extends LifecycleOwner> aClass = lifecycleOwner.getClass();
        //获取当前类所有方法
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            InjectBus injectBus = method.getAnnotation(InjectBus.class);
            if (injectBus != null) {
                String value = injectBus.value();
                boolean isActive = injectBus.isActive();

                //非活跃状态通知取消订阅
                if (!isActive) {
                    Observer remove = tempMap.remove(value);
                    if (remove != null) {
                        LiveDataBus.getInstance().getChannel(value).removeObserver(remove);
                    }
                }
            }
        }
    }

}
