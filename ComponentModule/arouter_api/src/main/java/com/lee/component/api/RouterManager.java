package com.lee.component.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import com.lee.component.annotation.model.RouterBean;
import com.lee.component.api.core.ARouterLoadGroup;
import com.lee.component.api.core.ARouterLoadPath;

/**
 * @author jv.lee
 * @date 2019-07-22
 * @description 路由管理器
 */
public class RouterManager {

    private static RouterManager instance;

    /**
     * 路由的组名
     */
    private String group;

    /**
     * 路由的路径
     */
    private String path;

    /**
     * Lru缓存，key：组名，value：路由组Group加载接口
     */
    private LruCache<String, ARouterLoadGroup> groupLruCache;

    /**
     * Lru缓存，key：路径名，value：路由Path路径加载接口
     */
    private LruCache<String, ARouterLoadPath> pathLruCache;

    /**
     * APT生成类文件前缀名
     */
    private static final String GROUP_FILE_PREFIX_NAME = ".ARouter$$Group$$";

    public static RouterManager getInstance() {
        if (instance == null) {
            synchronized (RouterManager.class) {
                if (instance == null) {
                    instance = new RouterManager();
                }
            }
        }
        return instance;
    }

    private RouterManager() {
        groupLruCache = new LruCache<>(180);
        pathLruCache = new LruCache<>(180);
    }

    /**
     * 传递路由的地址
     *
     * @param path
     * @return
     */
    public BundleManager build(String path) {
        if (TextUtils.isEmpty(path)) {
            Log.e(">>>", path);
            throw new IllegalArgumentException("未按照规范配置，如：/app/MainActivity");
        }

        group = subFromPath2Group(path);

        this.path = path;

        return new BundleManager();
    }

    /**
     * 截取组名
     *
     * @param path
     * @return
     */
    private String subFromPath2Group(String path) {
        //开发者写法：{path = "/MainActivity"}
        if (path.lastIndexOf("/") == 0) {
            throw new IllegalArgumentException("未按照规范配置，如：/app/MainActivity");
        }

        //从第一个/ 到第二个 / 中间截取组名 (app)
        String finalGroup = path.substring(1, path.indexOf("/", 1));

        if (TextUtils.isEmpty(finalGroup)) {
            throw new IllegalArgumentException("未按照规范配置，如：/app/MainActivity");
        }

        return finalGroup;
    }

    /**
     * 开始跳转
     *
     * @param context       上下文
     * @param bundleManager 参数管理器
     * @param code          requestCode/resultCode
     * @return 普通的跳转可以忽略，用于跨模块CALL接口
     */
    public Object navigation(Context context, BundleManager bundleManager, int code) {
        //ARouter$$Group$$order
        String groupClassName = context.getPackageName() + ".apt" + GROUP_FILE_PREFIX_NAME + group;
        Log.e("lee >>>", groupClassName);

        try {
            //读取路由组Group类文件（缓存，懒加载）
            ARouterLoadGroup groupLoad = groupLruCache.get(group);
            if (groupLoad == null) {
                //加载APT路由组Group类文件 ARouter$$Group$$personal
                Class<?> clazz = Class.forName(groupClassName);
                //初始化类文件
                groupLoad = (ARouterLoadGroup) clazz.newInstance();
                groupLruCache.put(group, groupLoad);
            }

            if (groupLoad.loadGroup().isEmpty()) {
                throw new RuntimeException("路由表Group加载失败");
            }

            //读取路由的Path路径类文件缓存（懒加载）
            ARouterLoadPath pathLoad = pathLruCache.get(path);
            if (pathLoad == null) {
                //通过组Group加载接口，获取Path加载接口
                Class<? extends ARouterLoadPath> clazz = groupLoad.loadGroup().get(group);

                //初始化
                if (clazz != null) {
                    pathLoad = clazz.newInstance();
                }
                if (pathLoad != null) {
                    pathLruCache.put(path, pathLoad);
                }
            }

            if (pathLoad != null) {
                if (pathLoad.loadPath().isEmpty()) {
                    throw new RuntimeException("路由表Path加载失败");
                }

                RouterBean routerBean = pathLoad.loadPath().get(path);
                if (routerBean != null) {
                    //类型判断，方便拓展
                    switch (routerBean.getType()) {
                        case ACTIVITY:
                            Intent intent = new Intent(context, routerBean.getClazz());
                            intent.putExtras(bundleManager.getBundle());

                            //startActivityForResult --> setResult
                            if (bundleManager.isResult()) {
                                ((Activity) context).setResult(code, intent);
                                ((Activity) context).finish();
                            }

                            //跳转需要回调
                            if (code > 0) {
                                ((Activity) context).startActivityForResult(intent, code, bundleManager.getBundle());
                            } else {
                                context.startActivity(intent,bundleManager.getBundle());
                            }

                            break;
                        case CALL:
                            //返回业务接口的实现类
                            return routerBean.getClass().newInstance();
                        default:
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
