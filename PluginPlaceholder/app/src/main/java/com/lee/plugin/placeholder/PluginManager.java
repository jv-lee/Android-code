package com.lee.plugin.placeholder;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * @author jv.lee
 * @date 2019-08-05
 * @description
 */
public class PluginManager {

    private static final String TAG = "PluginManager";

    private static PluginManager instance;

    private Context context;

    private PluginManager() {
    }

    private PluginManager(Context context) {
        this.context = context;
    }

    public static PluginManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager(context);
                }
            }
        }
        return instance;
    }

    private DexClassLoader dexClassLoader;
    private Resources resources;


    public void loadPlugin() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin.apk");
            if (!file.exists()) {
                Log.d(TAG, "loadPlugin: 插件包不存在...");
                return;
            }

            //插件包路径
            String pluginPath = file.getAbsolutePath();
            //插件包代码缓存路径
            File fileDir = context.getDir("pDir", Context.MODE_PRIVATE);

            //创建dexClassLoader 用于加载插件中的Activity.class
            dexClassLoader = new DexClassLoader(pluginPath, fileDir.getAbsolutePath(), null, context.getClassLoader());

            //加载插件包的路径 获取插件包中的 资源数据
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, pluginPath);

            //创建Resources 获取插件中的Resources
            Resources r = context.getResources();
            resources = new Resources(assetManager,r.getDisplayMetrics(),r.getConfiguration());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public Resources getResources() {
        return resources;
    }
}
