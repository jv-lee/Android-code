package com.gionee.gnservice.domain;

import android.os.AsyncTask;

/**
 * Created by caocong on 6/27/17.
 */
public abstract class Observable<T> extends AsyncTask<Object, Object, Object> {
    private static final String TAG = Observable.class.getSimpleName();
    private Observer<T> mObserver;

    public void setObserver(Observer observer) {
        mObserver = observer;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        if (values.length == 0) {
            return;
        }
        Object object = values[0];
        if (object instanceof Throwable) {
            mObserver.onError((Throwable) object);
            ((Throwable) object).printStackTrace();
        } else {
            mObserver.onNext((T) object);
        }
    }

    public void publishNext(T t) {
        publishProgress(t);
    }

    public void publishError(Throwable throwable) {

        publishProgress(throwable);
    }
}
