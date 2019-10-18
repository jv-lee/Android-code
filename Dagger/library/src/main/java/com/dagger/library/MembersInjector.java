package com.dagger.library;

/**
 * @author jv.lee
 * @date 2019/10/18.
 * @description
 */
public interface MembersInjector<T> {

    void injectMembers(T instance);
}
