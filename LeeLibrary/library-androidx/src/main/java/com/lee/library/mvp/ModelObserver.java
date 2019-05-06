package com.lee.library.mvp;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ModelObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {
        if (d.isDisposed()) {
            d.dispose();
        }
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
