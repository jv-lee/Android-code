package com.lee.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

/**
 * @author jv.lee
 * TODO RxJava 变换型操作符
 */
public class TransformActivity extends AppCompatActivity {

    private static final String TAG = "TransformActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transform);
    }

    /**
     * 数据类型变换操作
     */
    public void map(View view) {
        Disposable subscribe = Observable.just(1)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {

                        return "变换后的数据 -> " + integer;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "accept: " + s);
                    }
                });
    }


    public void flatMap(View view) {

        Disposable subscribe = Observable.just(1, 2, 3)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final Integer integer) throws Exception {

                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                //在此处重新处理操作 再发送数据
                                emitter.onNext("数据转换1：" + integer);
                                emitter.onNext("数据转换2：" + integer);
                                emitter.onNext("数据转换3：" + integer);
                            }
                        });
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String o) throws Exception {
                        Log.i(TAG, "accept: " + o);
                    }
                });
    }


    /**
     * flatMap 是没有排序的发射
     */
    public void flatMap2(View view) {
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("步惊云");
                emitter.onNext("雄霸");
                emitter.onNext("聂风");
                emitter.onComplete();
            }
        })
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add(s + " - 下标：" + (1 + i));
                        }
                        //延时发射
                        return Observable.fromIterable(list).delay(5, TimeUnit.SECONDS);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "accept: " + s);
                    }
                });
    }

    /**
     * concatMap 自带排序的发送
     */
    public void concatMap(View view) {
        Disposable subscribe = Observable.just("A", "B", "C")

                .concatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add(s + " - 下标：" + (1 + i));
                        }
                        return Observable.fromIterable(list).delay(5, TimeUnit.SECONDS);
                    }
                })

                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "accept: " + s);
                    }
                });
    }

    /**
     * groupBy 分组变换操作符
     */
    public void groupBy(View view) {

        Disposable subscribe = Observable.just(6000, 7000, 8000, 9000, 10000, 14000)
                .groupBy(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        //分组
                        return integer > 8000 ? "高端配置价格" : "中端配置价格";
                    }
                })

                .subscribe(new Consumer<GroupedObservable<String, Integer>>() {
                    @Override
                    public void accept(final GroupedObservable<String, Integer> groupedObservable) throws Exception {
                        //获取key
                        Log.i(TAG, "accept: " + groupedObservable.getKey());

                        //细节 GroupedObservable 被观察者
                        Disposable subscribe1 = groupedObservable.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.i(TAG, "accept: key:" + groupedObservable.getKey() + " value:" + integer);
                            }
                        });
                    }
                });
    }

    /**
     * 很多的数据，不想全部一起发射出去，分批次，先缓存到Buffer
     */
    public void buffer(View view) {

        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 100; i++) {
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        })
                //变换buffer
                .buffer(20)

                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Log.i(TAG, "accept: " + integers);
                    }
                });

    }

    public void elementAt(View view) {
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("九阴真经");
                emitter.onNext("九阳神功");
                emitter.onNext("一阳指");
                emitter.onNext("独孤九剑");
                emitter.onComplete();
            }
        })
                //zhidind
                .elementAt(2,"默认武功秘籍")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "accept: " + s);
                    }
                });
    }

}
