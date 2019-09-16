package com.lee.library;

import android.support.annotation.NonNull;

/**
 * @author jv.lee
 * @date 2019-09-16
 * @description
 */
public class ObservableMap<T> implements ObservableOnSubscribe<T> {

    private ObservableOnSubscribe<T> source;
    private Function<T, R> function;
    private Observer observer;

    ObservableMap(ObservableOnSubscribe<T> source, Function<T, R> function) {
        this.source = source;
        this.function = function;
    }

    @Override
    public void subscribe(@NonNull Observer emitter) throws Exception {
        this.observer = emitter;

        MapObserver<T> mapObserver = new MapObserver<>(emitter, source, function);

        source.subscribe(mapObserver);
    }

    class MapObserver<T> implements Observer<T> {

        private ObservableOnSubscribe<T> source;
        private Function<T, R> function;
        private Observer emitter;

        MapObserver(Observer emitter, ObservableOnSubscribe<T> source, Function<T, R> function) {
            this.emitter = emitter;
            this.source = source;
            this.function = function;
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(T item) {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }
    }
}

