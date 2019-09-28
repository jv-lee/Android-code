package com.lee.library;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

/**
 * @author jv.lee
 * @date 2019/9/28.
 * @description 下游切换android 主线程
 */
public class ObserverOnAndroidMain<T> implements ObservableOnSubscribe<T> {

    private ObservableOnSubscribe<T> source;

    public ObserverOnAndroidMain(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    public void subscribe(@NonNull Observer<T> emitter) throws Exception {
        PackageObserver<T> packageObserver = new PackageObserver<>(emitter);
        source.subscribe(packageObserver);
    }

    private final class PackageObserver<T> implements Observer<T> {

        /**
         * 拥有下一层的能力
         */
        private Observer<T> observer;

        public PackageObserver(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onSubscribe() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    observer.onSubscribe();
                }
            });
        }

        @Override
        public void onNext(final T item) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    observer.onNext(item);
                }
            });
        }

        @Override
        public void onError(final Throwable throwable) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    observer.onError(throwable);
                }
            });
        }

        @Override
        public void onComplete() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    observer.onComplete();
                }
            });
        }
    }
}
