package com.lee.component.api;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.LruCache;

import com.lee.component.api.core.ParameterLoad;

/**
 * @author jv.lee
 * @date 2019-07-22
 * @description 参数Parameter管理器
 */
public class ParameterManager {
    private static ParameterManager instance;

    /**
     * Lru缓存，key：类名，value：参数Parameter加载接口
     */
    private LruCache<String, ParameterLoad> cache;

    /**
     * APT生成的获取参数类名文件，后缀名
     */
    private static final String FILE_SUFFIX_NAME = "$$Parameter";

    /**
     * 全局单列
     *
     * @return ParameterManager
     */
    public static ParameterManager getInstance() {
        if (instance == null) {
            synchronized (ParameterManager.class) {
                if (instance == null) {
                    instance = new ParameterManager();
                }
            }
        }
        return instance;
    }

    private ParameterManager() {
        //初始化，并赋值缓存条目中的最大值
        cache = new LruCache<>(180);
    }

    /**
     * 加载参数
     * @param activity
     */
    public void loadParameter(@NonNull Activity activity) {
        String className = activity.getClass().getName();
        ParameterLoad iParameter = cache.get(className);

        try {
            //混存中找不到
            if (iParameter == null) {
                Class<?> clazz = Class.forName(className + FILE_SUFFIX_NAME);
                iParameter = (ParameterLoad) clazz.newInstance();
                cache.put(className, iParameter);
            }
            iParameter.loadParameter(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
