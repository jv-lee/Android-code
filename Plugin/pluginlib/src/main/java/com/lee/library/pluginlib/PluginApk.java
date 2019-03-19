package com.lee.library.pluginlib;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

public class PluginApk {
    //插件apk的实体对象
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
