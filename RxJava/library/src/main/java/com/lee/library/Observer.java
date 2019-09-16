package com.lee.library;

/**
 * @author jv.lee
 * @date 2019-09-16
 * @description 观察者 （下游）
 */
public interface Observer<T> {

    void onSubscribe();

    void onNext(T item);

    void onError(Throwable throwable);

    void onComplete();

}
