package com.lee.library.mvp;



import com.lee.library.utils.LogUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author jv.lee
 * @date 2019/5/6
 */
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
        LogUtil.getStackTraceString(e);
    }

    @Override
    public void onComplete() {

    }
}
