package com.lee.rxjava;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jv.lee
 * TODO RxJava 线程切换操作符
 */
public class ThreadActivity extends AppCompatActivity {

    private static final String TAG = "ThreadActivity";
    private static final String PATH = "https://img.mukewang.com/5d7aed120001f93916000540.jpg";
    private ImageView ivImage;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        ivImage = findViewById(R.id.iv_image);

        Log.i(TAG, "onCreate: threadName:" + Thread.currentThread().getName());
    }

    /**
     * Schedulers.io() : 代表io流操作，网络操作，文件流，耗时操作
     * Schedulers.newThread() : 创建一个普通线程
     * Schedulers.computation() : 代表CPU 大量计算 所需要的线程
     * AndroidSchedulers.mainThread() : android独有的 主线程切换
     *
     * @param view
     */
    public void thread(View view) {
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.i(TAG, "subscribe:上游 ThreadName:" + Thread.currentThread().getName());
                emitter.onNext("send event");
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "accept:下游 ThreadName:" + Thread.currentThread().getName());
                    }
                });
        subscribe.dispose();
    }

    /**
     * 异步加载网络图片
     *
     * @param view
     */
    public void loadImage(View view) {
        Observable.just(PATH)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        URL url = new URL(s);
                        URLConnection urlConnection = url.openConnection();
                        HttpURLConnection connection = (HttpURLConnection) urlConnection;
                        connection.setConnectTimeout(5000);
                        if (connection.getResponseCode() == 200) {
                            return BitmapFactory.decodeStream(connection.getInputStream());
                        }
                        return null;
                    }
                })
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(Bitmap bitmap) throws Exception {
                        //添加水印
                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setColor(Color.BLUE);
                        paint.setTextSize(30);
                        return drawTextToBitmap(bitmap, "绘制的水印", paint, 60, 60);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        progress = new ProgressDialog(ThreadActivity.this);
                        progress.setMessage("RxJava 下载图片中 ...");
                        progress.show();
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        if (ivImage != null) {
                            ivImage.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //设置错误图片站位图
                        if (ivImage != null) {
                            ivImage.setImageResource(R.mipmap.ic_launcher);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (progress != null) {
                            progress.dismiss();
                        }
                    }
                });
    }


    /**
     * 在Bitmap上绘制文字
     *
     * @param bitmap      原图
     * @param text        文字
     * @param paint       画笔
     * @param paddingLeft 向左偏移量
     * @param paddingTop  向上偏移量
     * @return 添加水印的图像
     */
    public Bitmap drawTextToBitmap(Bitmap bitmap, String text, Paint paint, int paddingLeft, int paddingTop) {
        Bitmap.Config config = bitmap.getConfig();

        //获取高清图像采样
        paint.setDither(true);
        //过滤像素
        paint.setFilterBitmap(true);
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(config, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }
}
