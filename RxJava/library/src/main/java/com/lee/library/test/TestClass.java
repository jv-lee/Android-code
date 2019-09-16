package com.lee.library.test;

import android.support.annotation.NonNull;

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
        just();

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
        Observable.craete(new ObservableOnSubscribe<Integer>() {
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

}
