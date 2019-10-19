package com.dagger.library;

/**
 * @author jv.lee
 * @date 2019-10-19
 * @description
 */
public class DoubleCheck<T> implements Provider<T> {

    private static final Object UNINITIALIZED = new Object();

    private volatile Provider<T> provider;
    private volatile Object instance = UNINITIALIZED;

    private DoubleCheck(Provider<T> provider) {
        this.provider = provider;
    }

    @Override
    public T get() {
        Object result = instance;
        if (result == UNINITIALIZED) {
            synchronized (this) {
                result = instance;
                if (result == UNINITIALIZED) {
                    instance = result = provider.get();
                    provider = null;
                }
            }
        }
        return (T) result;
    }

    public static <T> Provider<T> provider(Provider<T> delegate) {
        Preconditions.checkNotNull(delegate);
        if (delegate instanceof DoubleCheck) {
             return  delegate;
        }
        return new DoubleCheck<>(delegate);
    }
}
