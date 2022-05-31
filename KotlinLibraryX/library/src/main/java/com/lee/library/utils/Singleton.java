package com.lee.library.utils;

/**
 * 构造单列对象类
 * <p>get method:
 * <code>
 * public static T getService() {
 * return tSingleton.get();
 * }
 * </code>
 * <p/>
 *
 * <p>instance method:
 * <code>
 * private static final Singleton<T> tSingleton =
 * new Singleton<T>() {
 * protected T create() {
 * create T Object function()
 * return T;
 * }
 * };
 * </code>
 * <p/>
 * @author jv.lee
 * @date 2019/7/9.
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
