package com.examples.facade;

import com.examples.facade.impl.DiskCacheImpl;
import com.examples.facade.impl.MemoryCacheImpl;
import com.examples.facade.impl.NetWorkLoaderImpl;
import com.examples.facade.thing.DiskCache;
import com.examples.facade.thing.MemoryCache;
import com.examples.facade.thing.NetWorkLoader;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：
 */
public class MainActivityTest {

    private static final String URL = "https://www.163.com/logo.jpg";

    @Test
    public void onCreate() {
        //常规写法
        MemoryCache memoryCache = new MemoryCacheImpl();
        memoryCache.findByMemory(URL);

        DiskCache diskCache = new DiskCacheImpl();
        diskCache.findByDisk(URL);

        NetWorkLoader netWorkLoader = new NetWorkLoaderImpl();
        netWorkLoader.loadImageFromNet(URL);

        //装饰模式写法
        Facade facade = new Facade(URL);
        facade.load();
    }
}