package com.gionee.gnservice.domain;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.utils.PreconditionsUtil;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by caocong on 5/24/17.
 */
public class Case2 {
    protected IAppContext mAppContext;

    public Case2(IAppContext appContext) {
        mAppContext = appContext;
    }

    protected <T> void execute(Observable<T> observable, Observer<T> observer) {
        PreconditionsUtil.checkNotNull(observable);
        PreconditionsUtil.checkNotNull(observer);
        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
