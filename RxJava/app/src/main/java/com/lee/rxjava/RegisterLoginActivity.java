package com.lee.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jv.lee
 * TODO RxJava 注册切登陆
 */
public class RegisterLoginActivity extends AppCompatActivity {

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
    }

    /**
     * 伪代码
     * @param view
     */
    public void registerOrLogin(View view) {
        final RetrofitUtil retrofit = new RetrofitUtilImpl();

        retrofit.register()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //通过此api 直接处理ui 不直接订阅 观察者
                .doOnNext(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse registerResponse) throws Exception {
                        //todo 获取注册信息 更新ui
                    }
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
                    @Override
                    public ObservableSource<LoginResponse> apply(RegisterResponse registerResponse) throws Exception {
                        return retrofit.login();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        //TODO 显示progress

                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        //todo 处理登陆信息更新操作
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO 隐藏progress
                    }

                    @Override
                    public void onComplete() {
                        //TODO 隐藏progress
                    }
                });

    }

    private class RegisterResponse {
    }

    private class LoginResponse {
    }

    interface RetrofitUtil {
        Observable<LoginResponse> login();

        Observable<RegisterResponse> register();
    }

    class RetrofitUtilImpl implements RetrofitUtil {

        @Override
        public Observable<LoginResponse> login() {
            return null;
        }

        @Override
        public Observable<RegisterResponse> register() {
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
