package com.lee.glide;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.lee.glide.cache.ActiveCache;
import com.lee.glide.cache.MemoryCache;
import com.lee.glide.cache.MemoryCacheCallback;
import com.lee.glide.disk.DiskLruCacheImpl;
import com.lee.glide.lifecycle.LifycycleCallback;
import com.lee.glide.load.LoadDataManager;
import com.lee.glide.load.ResponseListener;
import com.lee.glide.resource.Key;
import com.lee.glide.resource.Value;
import com.lee.glide.resource.ValueCallback;

/**
 * @author jv.lee
 * @date 2019/9/2.
 * @description 请求资源 引擎
 */
public class RequestTargetEngine implements LifycycleCallback, ValueCallback, MemoryCacheCallback, ResponseListener {

    private static final String TAG = "RequestTargetEngine";

    private Context glideContext;

    private String path;

    private String key;

    private ImageView imageView;

    /**
     * 活动缓存
     */
    private ActiveCache activeCache;
    /**
     * 内存缓存
     */
    private MemoryCache memoryCache;
    /**
     * 磁盘缓存
     */
    private DiskLruCacheImpl diskLruCache;

    private final int MEMORY_MAX_SIZE = 1024 * 1024 * 60;


    RequestTargetEngine() {
        if (activeCache == null) {
            activeCache = new ActiveCache(this);
        }
        if (memoryCache == null) {
            memoryCache = new MemoryCache(MEMORY_MAX_SIZE);
            memoryCache.setMemoryCacheCallback(this);
        }
        //初始化磁盘缓存
        if (diskLruCache == null) {
            diskLruCache = new DiskLruCacheImpl();
        }
    }

    /**
     * 初始化缓存环境
     *
     * @param path
     * @param glideContext
     */
    void loadValueInitAction(String path, Context glideContext) {
        this.path = path;
        this.glideContext = glideContext;
        this.key = new Key(path).getKey();
    }

    void into(ImageView imageView) {
        this.imageView = imageView;

        if (imageView == null) {
            throw new NullPointerException("Glide into imageView is Null");
        }

        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("current thread check is MainThread ?");
        }

        Value value = cacheAction();
        if (null != value) {
            //使用完成
            value.nonUseAction();
            imageView.setImageBitmap(value.getBitmap());
        }
    }

    /**
     * 加载资源 -> 缓存 -> 网络/SD -> 加载资源成功后 -> 资源保存到缓存中
     *
     * @return 资源
     */
    private Value cacheAction() {
        //TODO 第一步，判断活动缓存是否有资源 有则直接返回
        Value value = activeCache.get(key);
        if (null != value) {
            Log.d(TAG, "cacheAction: 活动缓存中加载imageView resource success !");
            //使用一次 计数
            value.useAction();
            return value;
        }

        //TODO 第二步，判断内存缓存是否有资源 有则将资源移动到活动缓存再返回
        value = memoryCache.get(key);
        if (null != value) {
            //移动操作
            memoryCache.useRemove(key);
            activeCache.put(key, value);
            Log.d(TAG, "cacheAction: 内存缓存中加载imageView resource success !");
            //使用一次 计数
            value.useAction();
            return value;
        }

        //TODO 第三步，判断磁盘缓存是否有资源 有则将资源移动到互动缓存再返回
        value = diskLruCache.get(key);
        if (null != value) {
            //把磁盘缓存中的元素 -> 加入到活动缓存中
            activeCache.put(key, value);
            Log.d(TAG, "cacheAction: 磁盘缓存中加载imageView resource success !");
            //使用一次 计数
            value.useAction();
            return value;
        }

        //TODO 第四步，真正加载外部资源，网络/SD本地 加载
        value = new LoadDataManager().loadResource(path, this, glideContext);
        return value;
    }

    @Override
    public void glideInitAction() {
        Log.i(TAG, "glideInitAction: glide生命周期初始化");
    }

    @Override
    public void glideStopAction() {
        Log.i(TAG, "glideInitAction: glide生命周期停止");
    }

    @Override
    public void glideRecycleAction() {
        Log.i(TAG, "glideInitAction: glide生命周期回收");
        if (activeCache != null) {
            //释放活动缓存
            activeCache.closeThread();
        }

    }

    /**
     * 活动缓存中 不再使用Value后的回调
     *
     * @param key
     * @param value
     */
    @Override
    public void nonUse(String key, Value value) {
        //把活动缓存中操作的Value资源 加入到内存缓存
        if (key != null && value != null) {
            memoryCache.put(key, value);
        }
    }

    /**
     * 内存缓存中 被删除掉后的回调
     *
     * @param key
     * @param oldValue
     */
    @Override
    public void entryRemovedMemoryCache(String key, Value oldValue) {
        //添加到复用池 ....
    }

    @Override
    public void responseSuccess(Value value) {
        //使用一次 计数
        if (value != null) {
            saveCache(key, value);
            imageView.setImageBitmap(value.getBitmap());
        }
    }

    @Override
    public void responseError(Exception e) {
        Log.e(TAG, "responseError: ", e);
    }

    private void saveCache(String key, Value value) {
        value.setKey(key);

        //保存到磁盘缓存
        if (diskLruCache != null) {
            diskLruCache.put(key, value);
        }


    }
}
