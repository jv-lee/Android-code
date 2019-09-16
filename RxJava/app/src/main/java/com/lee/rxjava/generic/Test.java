package com.lee.rxjava.generic;

/**
 * @author jv.lee
 * @date 2019/9/16.
 * @description
 */
public class Test<T> {

    private T t;

    public T get() {
        return t;
    }

    public void add(T t) {
        this.t = t;
    }
}
