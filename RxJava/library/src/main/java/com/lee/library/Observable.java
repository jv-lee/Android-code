package com.lee.library;

import android.support.annotation.NonNull;

/**
 * @author jv.lee
 * @date 2019-09-16
 * @description 被观察者 (上游)
 */
public class Observable<T> {

    private ObservableOnSubscribe<T> source;

    private Observable(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        return new Observable<T>(source);
    }

    public static <T> Observable<T> just(final T... t) {
        return new Observable<T>(new ObservableOnSubscribe<T>() {

            @Override
            public void subscribe(@NonNull Observer emitter) throws Exception {
                for (T t1 : t) {
                    emitter.onNext(t1);
                }
                emitter.onComplete();
            }
        });
    }

    public static <T> Observable<T> just(final T t) {
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull Observer<T> emitter) throws Exception {
                emitter.onNext(t);
                emitter.onComplete();
            }
        });
    }

    public static <T> Observable<T> just(final T t, final T t2) {
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull Observer<T> emitter) throws Exception {
                emitter.onNext(t);
                emitter.onNext(t2);
                emitter.onComplete();
            }
        });
    }

    public static <T> Observable<T> just(final T t, final T t2, final T t3) {
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull Observer<T> emitter) throws Exception {
                emitter.onNext(t);
                emitter.onNext(t2);
                emitter.onNext(t3);
                emitter.onComplete();
            }
        });
    }

    public static <T> Observable<T> fromArray(final T[]... ts) {
        return new Observable<>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull Observer<T> emitter) throws Exception {
                for (T[] t : ts) {
                    for (T t1 : t) {
                        emitter.onNext(t1);
                    }
                }
                emitter.onComplete();
            }
        });
    }

    public <R> Observable<R> map(Function<T, R> function) {
        ObservableMap<T, R> observableMap = new ObservableMap<>(source, function);
        return new Observable<R>(observableMap);
    }

    public <R> Observable<R> flatMap(Function<T,R> function){
        ObservableFlatMap<T, R> observableMap = new ObservableFlatMap<>(source, function);
        return new Observable<>(observableMap);
    }

    public void subscribe(Observer<T> observer) {
        try {
            // 订阅成功
            observer.onSubscribe();
            source.subscribe(observer);
        } catch (Exception e) {
            e.printStackTrace();
            observer.onError(e);
        }
    }

    /**
     * 给上游分配子线程线程
     *
     * @return
     */
    public Observable<T> subscribeOn() {
        return create(new ObservableOnIo<T>(source));
    }

    /**
     * 给下游分配主线程
     *
     * @return
     */
    public Observable<T> observeOnAndroidMain() {
        ObserverOnAndroidMain<T> observerOnAndroidMain = new ObserverOnAndroidMain<>(source);
        return create(observerOnAndroidMain);
    }

}
