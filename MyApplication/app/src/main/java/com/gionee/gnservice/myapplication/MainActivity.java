package com.gionee.gnservice.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        justData();
//        loadData();
//        threadData();
        merge();
    }

    private void justData(){
        Observable.just(1, 3, 4, 5, 6)
                .map(i -> i + 1)
                .filter(i -> i < 5)
                .subscribe(new HttpResultObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        Log.i(">>>", "value:" + integer);
                    }
                });
    }

    private void loadData(){
        //三個任務同步執行
        Observable<String> ob1 = Observable.create(emitter -> emitter.onNext("1001"));
        Observable<String> ob2 = Observable.create(emitter -> emitter.onNext("1002"));
        Observable<String> ob3 = Observable.create(emitter -> emitter.onNext("1003"));
        HttpResultObserver<Object> httpResultObserver = Observable.zip(ob1, ob2, ob3, (Function3<String, String, String, Object>) (s, s2, s3) -> {
            String response = s + s2 + s3;
            Log.i(">>>", "合并后的数据"+response);
            return response;
        }).subscribeWith(new HttpResultObserver<Object>() {
            @Override
            public void onNext(Object o) {
                Log.i(">>>", "zip:" + o);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(">>>", "zip:" + e.getMessage());
            }
        });
    }

    private void threadData(){
        //实现 内存缓存、本地缓存、网络加载  三层数据
        String datas[] = {"memory", "disk", "network"};
        Observable<String> memoryObservable = Observable.create(emitter -> {
            String data = datas[0];
            emitter.onNext(data);
            emitter.onComplete();
        });

        Observable<String> diskObservable = Observable.create(emitter -> {
            String data = datas[1];
            emitter.onNext(data);
            emitter.onComplete();
        });

        Observable<String> networkObservable = Observable.create((ObservableOnSubscribe<String>) emitter -> {
            String data = datas[2];
            emitter.onNext(data);
            emitter.onComplete();
        }).observeOn(Schedulers.io());

        //按顺序执行 全部数据到位后 回调 onComplete();
        Observable.concat(memoryObservable, diskObservable, networkObservable)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.i(">>>", "三级缓存:" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(">>>", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(">>>", "缓存：onComplete()");
                    }
                });

        //从 内存 磁盘 网络获取数据 按顺序执行 第一个不为空时就不再需要后面的数据 直接返回
        Disposable obj = Observable.concat(memoryObservable, diskObservable, networkObservable)
                .first("obj")
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(">>>", "三级缓存中获取到的数据：" + s);
                    }
                });

        Observable<Object> zip = Observable.zip(memoryObservable, diskObservable, new BiFunction<String, String, Object>() {
            @Override
            public Object apply(String s, String s2) throws Exception {
                return s + s2;
            }
        });

        HttpResultObserver<Object> httpResultObserver1 = networkObservable.flatMap(new Function<String, ObservableSource<Object>>() {
            @Override
            public ObservableSource<Object> apply(String s) throws Exception {
                Log.i(">>>", "data1:" + s);
                return zip;
            }
        }).subscribeWith(new HttpResultObserver<Object>() {
            @Override
            public void onNext(Object o) {
                Log.i(">>>", "data2:"+o);
            }
        });
    }

    public void merge(){
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add("数据："+i);
        }
        List<String> reData = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            data.add("数据："+i);
        }

        Observable<List<String>> dataObservable = Observable.fromArray(data);
        Observable<List<String>> reDataObservable = Observable.fromArray(reData);
        Observable<List<String>> mergeObservable = Observable.merge(dataObservable, reDataObservable);

        mergeObservable.subscribe(new HttpResultObserver<List<String>>() {
            @Override
            public void onNext(List<String> strings) {
                for (String string : strings) {
                    Log.i(">>>", string);
                }
            }
        });

    }

}
