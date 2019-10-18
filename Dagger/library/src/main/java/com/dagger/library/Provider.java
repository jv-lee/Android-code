package com.dagger.library;

/**
 * @author jv.lee
 * @date 2019/10/17.
 * @description
 */
public interface Provider<T> {
    T get();
}
