package com.lee.library.utils;

/**
 * @author jv.lee
 * @date 2019/7/9.
 * @description 构造单列对象类
 * <p>
 * public static T getService() {
 * return tSingleton.get();
 * }
 * <p>
 * private static final Singleton<T> tSingleton =
 * new Singleton<T>() {
 * @Override protected T create() {
 * create T Object function()
 * return T;
 * }
 * };
 */
public abstract class Singleton<T> {

    private T mInstance;

    /**
     * 创建实例对象 提供给实现类
     *
     * @return T
     */
    protected abstract T create();

    public final T get() {
        synchronized (this) {
            if (mInstance == null) {
                mInstance = create();
            }
            return mInstance;
        }
    }
}
