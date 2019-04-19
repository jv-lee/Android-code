package com.gionee.gnservice.myapplication;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class HttpResultObserver<T> implements Observer<T>  {
    @Override
    public void onSubscribe(Disposable d) {

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
