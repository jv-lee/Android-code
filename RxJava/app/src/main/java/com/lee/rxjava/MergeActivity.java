package com.lee.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * @author jv.lee
 * TODO RxJava 合并型操作符
 */
public class MergeActivity extends AppCompatActivity {

    private static final String TAG = "MergeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge);
    }


    /**
     * 先执行被观察者2 ， 再执行被观察者1
     *
     * @param view
     */
    public void startWith(View view) {

        //再执行被观察者1的事件
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
            //先执行被观察者2的事件
        }).startWith(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1000);
                emitter.onNext(2000);
                emitter.onNext(3000);
                emitter.onComplete();
            }
        }))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * 先执行被观察者1 ， 再执行被观察者2
     *
     * @param view
     */
    public void concatWith(View view) {
        //先执行被观察者1
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
            //再执行被观察者2
        }).concatWith(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1000);
                emitter.onNext(2000);
                emitter.onNext(3000);
                emitter.onComplete();
            }
        }))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * 合并4个被观察者按顺序执行
     *
     * @param view
     */
    public void concat(View view) {
        //最多存储4个被观察者
        Disposable subscribe = Observable.concat(
                Observable.just(1),
                Observable.just(2),
                Observable.just(3),
                Observable.just(4)
        )
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * 合并后 并列执行
     *
     * @param view
     */
    public void merge(View view) {
        //被观察者1
        Observable<Long> observable1 = Observable.intervalRange(1, 5, 1, 2, TimeUnit.SECONDS);

        //被观察者2
        Observable<Long> observable2 = Observable.intervalRange(6, 5, 1, 2, TimeUnit.SECONDS);

        //被观察者3
        Observable<Long> observable3 = Observable.intervalRange(11, 5, 1, 2, TimeUnit.SECONDS);

        //并列执行
        Disposable subscribe = Observable.merge(observable1, observable2, observable3)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i(TAG, "accept: " + aLong);
                    }
                });
    }

    /**
     * zip 被观察者合并成一个 开始发送事件
     * 数量合并的被观察者发送的事件 要一一对应
     *
     * @param view
     */
    public void zip(View view) {

        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("英语");
                emitter.onNext("数学");
                emitter.onNext("政治");

                //没有对应的成绩 被忽略了
                emitter.onNext("物理");
                emitter.onComplete();
            }
        });

        Observable<Integer> observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(95);
                emitter.onNext(98);
                emitter.onNext(99);
                emitter.onComplete();
            }
        });

        Observable.zip(observable1, observable2, new BiFunction<String, Integer, StringBuffer>() {
            @Override
            public StringBuffer apply(String s, Integer integer) throws Exception {

                return new StringBuffer(s).append(":").append(integer);
            }
        }).subscribe(new Observer<StringBuffer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: 准备进入考场考试...");
                if (d.isDisposed()) {
                    d.dispose();
                }
            }

            @Override
            public void onNext(StringBuffer stringBuffer) {
                Log.i(TAG, "onNext: 考试结果输出->" + stringBuffer.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: 考试作弊被抓了...");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: 考试全部结束");
            }
        });

    }

}
