package com.lee.code.livedatabus.livedata;


import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

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
    private Map<String,LiveData<Object>> bus;
    private static LiveDataBus instance;
    private LiveDataBus() {
        bus = new HashMap<>();
    }
    public static LiveDataBus getInstance(){
        if (instance == null) {
            synchronized (LiveDataBus.class) {
                if (instance == null) {
                    instance = new LiveDataBus();
                }
            }
        }
        return instance;
    }

    public <T> LiveData<T> getChannel(String target, Class<T> type) {
        if (!bus.containsKey(target)) {
            bus.put(target, new LiveData<Object>());
        }
        return (LiveData<T>) bus.get(target);
    }

    public LiveData<Object> getChannel(String target) {
        return getChannel(target, Object.class);
    }


    public void injectBus(Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();
        //获取当前类所有方法
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            InjectBus injectBus = method.getAnnotation(InjectBus.class);
            if (injectBus != null) {
                String value = injectBus.value();

                LiveDataBus.getInstance().getChannel(value).observe(activity, new Observer<Object>() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        try {
                            method.invoke(activity, o);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public void injectBus(Fragment fragment) {
        Class<? extends Fragment> aClass = fragment.getClass();
        //获取当前类所有方法
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            InjectBus injectBus = method.getAnnotation(InjectBus.class);
            if (injectBus != null) {
                String value = injectBus.value();

                LiveDataBus.getInstance().getChannel(value).observe(fragment.getActivity(), new Observer<Object>() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        try {
                            method.invoke(fragment, o);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

}
