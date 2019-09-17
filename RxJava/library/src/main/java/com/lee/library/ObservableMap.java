package com.lee.library;

import android.support.annotation.NonNull;

/**
 * @author jv.lee
 * @date 2019-09-16
 * @description
 */
public class ObservableMap<T,R> implements ObservableOnSubscribe<R> {

    private ObservableOnSubscribe<T> source;
    private Function<T, R> function;
    private Observer<R> observer;

    ObservableMap(ObservableOnSubscribe<T> source, Function<T, R> function) {
        this.source = source;
        this.function = function;
    }

    @Override
    public void subscribe(@NonNull Observer<R> emitter) throws Exception {
        this.observer = emitter;

        MapObserver<T> mapObserver = new MapObserver(emitter, source, function);

        source.subscribe(mapObserver);
    }

    class MapObserver<T> implements Observer<T> {

        private ObservableOnSubscribe<T> source;
        private Function<T, R> function;
        private Observer<R> emitter;

        MapObserver(Observer<R> emitter, ObservableOnSubscribe<T> source, Function<T, R> function) {
            this.emitter = emitter;
            this.source = source;
            this.function = function;
        }

        @Override
        public void onSubscribe() {
            emitter.onSubscribe();
        }

        @Override
        public void onNext(T item) {
            try {
                R nextMapResult = function.apply(item);
                emitter.onNext(nextMapResult);
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            emitter.onError(throwable);
        }

        @Override
        public void onComplete() {
            emitter.onComplete();
        }
    }
}

