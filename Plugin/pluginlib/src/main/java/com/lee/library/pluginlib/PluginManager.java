package com.lee.library.pluginlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * @author jv.lee
 */
public class PluginManager {

    private static PluginManager mInstance;

    public static PluginManager getInstance(){
        if (mInstance == null) {
            synchronized (PluginManager.class) {
                if (mInstance == null) {
                    mInstance = new PluginManager();
                }
            }
        }
        return mInstance;
    }

    private PluginManager(){}

    private Context mContext;
    private PluginApk mPluginApk;

    public void init(Context context) {
        mContext = context;
    }


    /**
     * 加载插件apk
     * @param apkPath
     */
    public void loadApk(String apkPath) {
        PackageInfo packageInfo = mContext.getPackageManager().getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        if (packageInfo == null) {
            return;
        }
        DexClassLoader classLoader = createDexClassLoader(apkPath);
        AssetManager am = createAssetManager(apkPath);
        Resources resources = createResources(am);
        mPluginApk = new PluginApk(packageInfo, resources, classLoader);
    }

    public PluginApk getPluginApk(){
        return mPluginApk;
    }

    private Resources createResources(AssetManager am) {
        Resources res = mContext.getResources();
        return new Resources(am, res.getDisplayMetrics(), res.getConfiguration());
    }

    private AssetManager createAssetManager(String apkPath) {
        try {
            AssetManager am = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
            method.invoke(am, apkPath);
            return am;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param apkPath
     * @return
     */
    private DexClassLoader createDexClassLoader(String apkPath) {
        File file = mContext.getDir("dex", Context.MODE_PRIVATE);
        return new DexClassLoader(apkPath,file.getAbsolutePath(),null,mContext.getClassLoader());
    }




    //创建Resource对象
}
