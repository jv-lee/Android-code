package com.lee.code.glide.request;

import android.content.Context;
import android.widget.ImageView;

import com.lee.code.glide.listener.RequestListener;
import com.lee.code.glide.utils.MD5Utils;

import java.lang.ref.SoftReference;

public class BitmapRequest {
    //缓存不能通过 url作为key  需要转换成md5 来作为key
    private String uri;
    private SoftReference<ImageView> softReference;
    private String uriMD5;
    //正在加载时等待图片
    private int loadingResId;
    private Context context;

    private RequestListener requestListener;

    public BitmapRequest(Context context) {
        this.context = context;
    }

    public BitmapRequest loading(int loadingResId) {
        this.loadingResId = loadingResId;
        return this;
    }

    public BitmapRequest listener(RequestListener requestListener) {
        this.requestListener = requestListener;
        return this;
    }

    public BitmapRequest load(String url) {
        this.uri = url;
        this.uriMD5 = MD5Utils.hashKeyForDisk(url);
        return this;
    }

    public void into(ImageView imageView) {
        this.softReference = new SoftReference<>(imageView);
        imageView.setTag(uriMD5);
        RequestManager.getInstance().addBitmapRequest(this);
    }

    public String getUri() {
        return uri;
    }

    public ImageView getImageView(){
        return softReference.get();
    }

    public SoftReference<ImageView> getSoftReference() {
        return softReference;
    }

    public String getUriMD5() {
        return uriMD5;
    }

    public int getLoadingResId() {
        return loadingResId;
    }

    public Context getContext() {
        return context;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }
}
