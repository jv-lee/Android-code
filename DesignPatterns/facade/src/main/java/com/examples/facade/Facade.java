package com.examples.facade;

import com.examples.facade.impl.DiskCacheImpl;
import com.examples.facade.impl.MemoryCacheImpl;
import com.examples.facade.impl.NetWorkLoaderImpl;
import com.examples.facade.thing.DiskCache;
import com.examples.facade.thing.MemoryCache;
import com.examples.facade.thing.NetWorkLoader;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description： 外观模式（门面模式）
 * 隐藏系统的复杂性，为子系统中的一组接口提供了一个统一的访问接口
 * 高聚合，低耦合
 * 模拟场景 图片加载
 */
public class Facade {

    private String url;
    private MemoryCache memoryCache;
    private DiskCache diskCache;
    private NetWorkLoader netWorkLoader;

    public Facade(String url) {
        this.url = url;
        memoryCache = new MemoryCacheImpl();
        diskCache = new DiskCacheImpl();
        netWorkLoader = new NetWorkLoaderImpl();
    }

    void load(){
        memoryCache.findByMemory(url);
        diskCache.findByDisk(url);
        netWorkLoader.loadImageFromNet(url);
    }

}
