package com.gionee.gnservice.domain;

/**
 * Created by caocong on 6/27/17.
 */
public interface Observer<T> {

    void onCompleted();

    void onError(Throwable var1);

    void onNext(T var1);
}
