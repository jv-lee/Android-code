package com.lee.library.hermeslib;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 1.缓存中心设计
 * 缓存中心
 * 缓存类文件 类方法
 */
public class CacheCenter {
    private HashMap<String, Class<?>> mClasses = new HashMap<>();
    private HashMap<Class<?>, HashMap<String, Method>> mMethods = new HashMap<>();
    private HashMap<String, Object> mObjects = new HashMap<>();

    private CacheCenter(){}
    private static CacheCenter instance;
    public static CacheCenter getInstance(){
        if (instance == null) {
            synchronized (CacheCenter.class) {
                if (instance == null) {
                    instance = new CacheCenter();
                }
            }
        }
        return instance;
    }

    /**
     * 类注册方法
     * @param clazz
     */
    public void register(Class<?> clazz) {
        mClasses.put(clazz.getName(), clazz);
        registerMethod(clazz);
    }

    /**
     * 函数注册方法
     * @param clazz 对象类
     */
    @TargetApi(Build.VERSION_CODES.N)
    private void registerMethod(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            //通过类创建一个属于该类的 HashMap 存储当前类所有的方法
            mMethods.putIfAbsent(clazz, new HashMap<String, Method>());
            //map都不会为空
            HashMap<String, Method> map = mMethods.get(clazz);
            map.put(method.getName(), method);
        }
    }

    /**
     * 获取某类的方法
     * @param className 对象类名称 key
     * @param name 方法名称 key
     * @return
     */
    @TargetApi(Build.VERSION_CODES.N)
    public Method getMethod(String className, String name) {
        Class clazz = getClassType(className);
        if (name != null) {
            Log.i("lee >>> ", "getMethod:1=====" + name);
            mMethods.putIfAbsent(clazz, new HashMap<String, Method>());
            HashMap<String, Method> methods = mMethods.get(clazz);
            Method method = methods.get(name);
            if (method != null) {
                return method;
            }
        }
        return null;
    }

    public Class getClassType(String className) {
        if (TextUtils.isEmpty(className)) {
            return null;
        }
        Class<?> clazz = mClasses.get(className);
        if (clazz == null) {
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }

    public Object getObject(String name) {
        return mObjects.get(name);
    }

    public void putObject(String name, Object object) {
        mObjects.put(name, object);
    }
}
