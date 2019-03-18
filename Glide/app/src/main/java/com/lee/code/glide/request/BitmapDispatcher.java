package com.lee.code.glide.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.lee.code.glide.GlideApplication;
import com.lee.code.glide.cache.DoubleLruCache;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

//消费者 消费请求需求  （请求需求就是生产者 生产需求）
public class BitmapDispatcher extends Thread {

    DoubleLruCache doubleCache = DoubleLruCache.getInstance(GlideApplication.getInstance());
    private BlockingQueue<BitmapRequest> requestQueue;
    private Handler handler = new Handler(Looper.getMainLooper());

    public BitmapDispatcher(BlockingQueue<BitmapRequest> requestBlockingQueue) {
        this.requestQueue = requestBlockingQueue;
    }

    @Override
    public void run() {

        while (!isInterrupted()) {
            try {
                //阻塞队列
                BitmapRequest request = requestQueue.take();
                //先展示 loading 子线程 -> UI线程
                showLoadingImage(request);
                //加载图片
                Bitmap bitmap = findBitmap(request);
                //显示UI
                deliveryUIThread(request,bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private Bitmap findBitmap(BitmapRequest request) {
        //通过缓存机制取图片
        Bitmap bitmap = doubleCache.get(request);
        if (bitmap != null) {
            return bitmap;
        }

        //网络下载一张图片
        bitmap = downloadimage(request.getUri());
        if (bitmap != null) { //放进二级缓存中
            doubleCache.put(request,bitmap);
        }
        return bitmap;
    }

    private Bitmap downloadimage(String uri) {
        FileOutputStream fos = null;
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    private void deliveryUIThread(final BitmapRequest request, final Bitmap bitmap) {
        //UI线程显示
        handler.post(new Runnable() {
            @Override
            public void run() {
                //防止图片错位
                ImageView imageview = request.getImageView();
                if (imageview != null && bitmap != null && imageview.getTag().equals(request.getUriMD5())) {
                    imageview.setImageBitmap(bitmap);
                }
            }
        });
        /**
         * 监听图片是否请求成功
         */
        if (request.getRequestListener() != null) {
            if (bitmap != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        request.getRequestListener().onResourceReady(bitmap);
                    }
                });
            }else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        request.getRequestListener().onException();
                    }
                });
            }
        }
    }

    //展示Loading画面
    private void showLoadingImage(BitmapRequest request) {
        if (request.getLoadingResId() > 0) {
            final ImageView imageView = request.getImageView();
            final int resId = request.getLoadingResId();
            if (imageView != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI　线程
                        imageView.setImageResource(resId);
                    }
                });
            }
        }
    }
}
