package com.lee.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;


/**
 * @author jv.lee
 * TODO RxJava 条件型操作符
 */
public class ConditionActivity extends AppCompatActivity {

    private static final String TAG = "ConditionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);
    }

    /**
     *  all与any相反
     * @param view
     */
    public void all(View view) {
        Disposable subscribe = Observable.just(1, 2, 3, 4, 5)
                //所有参数 全部满足才返回true
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer == 1;
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean flat) throws Exception {
                        Log.i(TAG, "accept: " + flat);
                    }
                });
    }

    /**
     * any 与all相反
     * @param view
     */
    public void any(View view) {
        Disposable subscribe = Observable.just(1, 2, 3, 4, 5)
                //任意条件满足 返回true
                .any(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer == 1;
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.i(TAG, "accept: "+aBoolean);
                    }
                });
    }

    public void contains(View view) {
        Disposable subscribe = Observable.just("JavaSE", "JavaEE", "JavaME", "Android", "iOS", "Rect.js", "NDK")
                //是否包含
                .contains("Android")
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean s) throws Exception {
                        Log.i(TAG, "accept: " + s);
                    }
                });
    }

}
