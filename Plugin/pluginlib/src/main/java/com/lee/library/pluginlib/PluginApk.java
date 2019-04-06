package com.lee.library.pluginlib;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

/**
 * 插件化apk 实例
 * @author jv.lee
 */
public class PluginApk {

    public PackageInfo mPackageInfo;
    public Resources mResources;
    public AssetManager mAssetsManager;
    public DexClassLoader mClassLoader;

    public PluginApk(PackageInfo mPackageInfo, Resources mResources, DexClassLoader mClassLoader) {
        this.mPackageInfo = mPackageInfo;
        this.mResources = mResources;
        this.mClassLoader = mClassLoader;
    }
}
