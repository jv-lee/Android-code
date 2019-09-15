package com.lee.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author jv.lee
 * TODO RxJava 异常处理操作符
 */
public class ExceptionActivity extends AppCompatActivity {

    private static final String TAG = "ExceptionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);
    }

    /**
     * onErrorReturn ：1.能够接收e.onError , 2.如果接收到异常，会中断上游后续发射的所有事件
     * 发送错误标记
     *
     * @param view
     */
    public void onErrorReturn(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 100; i++) {
                    if (i == 5) {
                        emitter.onError(new IllegalAccessError("主动抛出异常"));
                    }
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        })
                //异常操作符
                .onErrorReturn(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable) throws Exception {
                        //错误清空返回400标记
                        return 400;
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });

    }

    /**
     * onErrorResumeNext
     * 错误后返回一个被观察者 实现更多的操作
     *
     * @param view
     */
    public void onErrorResumeNext(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 100; i++) {
                    if (i == 5) {
                        emitter.onError(new Error("主动抛出错误"));
                    } else {
                        emitter.onNext(i);
                    }
                }
                emitter.onComplete();
            }
        })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
                    @Override
                    public ObservableSource<? extends Integer> apply(Throwable throwable) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                                emitter.onNext(400);
                                emitter.onNext(400);
                                emitter.onNext(400);
                                emitter.onComplete();
                            }
                        });
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    /**
     * 下游返回观察者
     * 可以主动抛出异常 下游可接收 常用形式
     *
     * @param view
     */
    public void onExceptionResumeNext(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 100; i++) {
                    if (i == 5) {
                        emitter.onError(new IllegalAccessException("主动抛出错误"));
                    } else {
                        emitter.onNext(i);
                    }
                }
                emitter.onComplete();
            }
        })
                .onExceptionResumeNext(new ObservableSource<Integer>() {
                    @Override
                    public void subscribe(Observer<? super Integer> observer) {
                        observer.onNext(400);
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    /**
     * 重试操作符 ， 也分配为异常处理操作符
     *
     * @param view
     */
    public void retry(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 100; i++) {
                    if (i == 5) {
                        emitter.onError(new IllegalAccessException("主动抛出错误"));
                    } else {
                        emitter.onNext(i);
                    }
                }
                emitter.onComplete();
            }
        })
                //TODO 只判定返回 无限重试或不重试
//                .retry(new Predicate<Throwable>() {
//                    @Override
//                    public boolean test(Throwable throwable) throws Exception {
//                        Log.i(TAG, "test: " + throwable.getMessage());
//                        //返回false代表不重试
//                        return false;
//                        //return true 一直重试
//                    }
//                })

                //TODO 重试3次
//                .retry(3,new Predicate<Throwable>() {
//                    @Override
//                    public boolean test(Throwable throwable) throws Exception {
//                        Log.i(TAG, "test: " + throwable.getMessage());
//                        //返回false代表不重试
//                        return true;
//                        //return true 一直重试
//                    }
//                })

                //TODO 显示重试次数和异常
                .retry(new BiPredicate<Integer, Throwable>() {
                    @Override
                    public boolean test(Integer integer, Throwable throwable) throws Exception {
                        Log.i(TAG, "test: count:" + integer + " exception:" + throwable.getMessage());
                        return true;
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

}
