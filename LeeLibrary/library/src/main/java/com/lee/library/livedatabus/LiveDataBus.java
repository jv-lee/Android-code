package com.lee.library.livedatabus;


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

}
