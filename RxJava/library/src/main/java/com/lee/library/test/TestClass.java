package com.lee.library.test;

import android.support.annotation.NonNull;

import com.lee.library.Function;
import com.lee.library.Observable;
import com.lee.library.ObservableOnSubscribe;
import com.lee.library.Observer;

/**
 * @author jv.lee
 * @date 2019-09-16
 * @description
 */
public class TestClass {

    public static void main(String[] args) {

//        create();
//        just();
        map();
    }

    private static void just() {
        Observable.just("code 1 ", "code2")
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe() {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(String item) {
                        System.out.println("onNext:" + item);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("onError:" + throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }


    private static void create() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull Observer<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe() {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(Integer item) {
                        System.out.println("onNext:" + item);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("onError:" + throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    private static void map() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull Observer<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onComplete();
            }
        })
                .map(new Function<Integer, String>() {
                    @NonNull
                    @Override
                    public String apply(@NonNull Integer integer) throws Exception {
                        return "this is index - > " + integer;
                    }
                })
                .map(new Function<String, String>() {
                    @NonNull
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        return s+ " --------------";
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe() {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(String item) {
                        System.out.println("onNext:" + item);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("onError:" + throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }
}
