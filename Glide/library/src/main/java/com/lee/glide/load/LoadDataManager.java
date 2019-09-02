package com.lee.glide.load;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.lee.glide.resource.Value;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jv.lee
 * @date 2019-09-02
 * @description 资源加载管理类
 */
public class LoadDataManager implements ILoadData, Runnable {

    private String path;
    private ResponseListener responseListener;
    private Context context;

    @Override
    public Value loadResource(String path, ResponseListener responseListener, Context context) {
        this.path = path;
        this.responseListener = responseListener;
        this.context = context;

        //加载网络图片
        networkLoad();

        //本地SD图片

        return null;
    }

    private void networkLoad() {
        Uri uri = Uri.parse(path);
        if ("HTTP".equalsIgnoreCase(uri.getScheme()) ||
                "HTTPS".equalsIgnoreCase(uri.getScheme())) {
            new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                    60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable runnable) {
                            return new Thread(runnable, "Glide Thread");
                        }
                    })
                    .execute(this);
        }
    }

    @Override
    public void run() {
        InputStream is = null;
        try {
            URL url = new URL(path);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = (HttpURLConnection) urlConnection;
            connection.setConnectTimeout(5000);
            final int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                is = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                final Value value = Value.getInstance();
                value.setBitmap(bitmap);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        responseListener.responseSuccess(value);
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        responseListener.responseError(new RuntimeException("network load response errorCode :" + responseCode));
                    }
                });
            }

        } catch (final Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    responseListener.responseError(e);
                }
            });
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

}
