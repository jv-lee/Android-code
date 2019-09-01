package com.lee.glide.cache;

import com.lee.glide.resource.Value;
import com.lee.glide.resource.ValueCallback;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jv.lee
 * @date 2019-09-01
 * @description 活动缓存 -- 真正被使用的资源
 */
public class ActiveCache {

    private Map<String, WeakReference<Value>> cacheMap = new HashMap<>();
    /**
     * 为了监听若引用是否被回收
     */
    private ReferenceQueue<Value> queue;

    private boolean isCloseThread;
    private boolean isActionRemove;
    private ValueCallback callback;
    private ExecutorService executorService;

    public ActiveCache(ValueCallback callback) {
        this.callback = callback;
    }

    /**
     * TODO 资源添加到活动缓存
     *
     * @param key
     * @param value
     */
    public void put(String key, Value value) {
        //Tool 判断key 不能为空

        //绑定监听 -> Value发起监听操作 返回监听
        value.setCallback(callback);

        //存储
        cacheMap.put(key, new CustomizeWeakReference(value, getQueue(), key));
    }

    /**
     * TODO 获取活动缓存中的资源
     *
     * @param key
     * @return
     */
    public Value get(String key) {
        WeakReference<Value> valueWeakReference = cacheMap.get(key);
        if (valueWeakReference != null) {
            return valueWeakReference.get();
        }
        return null;
    }

    /**
     * TODO 手动删除活动缓存中的资源 主动回收
     *
     * @param key
     * @return
     */
    public Value remove(String key) {
        isActionRemove = true;
        WeakReference<Value> valueWeakReference = cacheMap.remove(key);
        //还原状态 让线程继续工作 GC操作
        isActionRemove = false;
        if (valueWeakReference != null) {
            return valueWeakReference.get();
        }
        return null;
    }

    /**
     * 关闭线程
     */
    public void closeThread() {
        isCloseThread = true;
//        if (null != executorService) {
//            executorService.shutdownNow();
//        }

        cacheMap.clear();
        System.gc();
    }


    /**
     * 通过自定义弱引用类 queue构造参数监听是否被回收
     */
    private class CustomizeWeakReference extends WeakReference<Value> {

        private String key;

        CustomizeWeakReference(Value referent, ReferenceQueue<? super Value> queue, String key) {
            super(referent, queue);
            this.key = key;
        }
    }

    /**
     * 监听弱引用是否被回收  / 被动回收
     *
     * @return
     */
    private ReferenceQueue<Value> getQueue() {
        if (queue == null) {
            queue = new ReferenceQueue<>();

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    //线程是否被关闭 关闭后不再监听阻塞方法
                    while (!isCloseThread) {
                        try {
                            //isActionRemove区分手动移除和被动移除  false被动移除
                            if (!isActionRemove) {
                                //已经被回收了 该方法被执行 , 未被回收该方法阻塞 queue.remove()
                                Reference<? extends Value> remove = queue.remove();
                                CustomizeWeakReference weakReference = (CustomizeWeakReference) remove;

                                //移除容器中包含的资源
                                if (cacheMap != null && !cacheMap.isEmpty()) {
                                    cacheMap.remove(weakReference.key);
                                }
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        }
        return queue;
    }

}
