package com.lee.library.tool;

/**
 * @author jv.lee
 * @date 2019/7/9.
 * @description 构造单列对象类
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
