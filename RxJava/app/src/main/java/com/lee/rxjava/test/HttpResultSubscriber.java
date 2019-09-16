package com.lee.rxjava.test;


import org.reactivestreams.Subscription;

import io.reactivex.FlowableSubscriber;

public abstract class HttpResultSubscriber<T> implements FlowableSubscriber<T> {

    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onNext(T t) {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onComplete() {

    }

}
