package com.lee.library;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * @author jv.lee
 * @date 2019-07-16
 * @description
 */
public class SkinManager {

    private static SkinManager instance;
    private Application application;
    private Resources appResources;
    private Resources skinResources;
    private String skinPackageName;
    private boolean isDefaultSkin = true;

    private static final String ADD_ASSET_PATH = "addAssetsPath";

    private SkinManager(Application application) {
        this.application = application;
        appResources = application.getResources();
    }

    public static SkinManager getInstance(Application application) {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager(application);
                }
            }
        }
        return instance;
    }

    public static SkinManager getInstance() {
        return instance;
    }

    /**
     * 加载皮肤包资源
     *
     * @param skinPath 皮肤包路径，为空则为加载app内置资源
     */
    public void loaderSkinResoures(String skinPath) {
        //如果没有皮肤包或者没做换肤动作，方法不执行
        if (TextUtils.isEmpty(skinPath)) {
            isDefaultSkin = true;
            return;
        }
        try {
            //创建资源管理器
            AssetManager assetManager = AssetManager.class.newInstance();
            // 该方法被系统@hide ， 所以通过反射获取 : 如果后续版本被限制 可以反射addAssetsInternal()方法，源码366行+387行
            Method addAssetPath = assetManager.getClass().getDeclaredMethod(ADD_ASSET_PATH, String.class);
            //设置私有方法可以访问
            addAssetPath.setAccessible(true);
            //执行addAssetPath方法（才能把外面的皮肤包加入到本应用）
            addAssetPath.invoke(assetManager, skinPath);

            //创建加载外部皮肤包资源（skin.apk.skin） 文件Resource
            skinResources = new Resources(assetManager,
                    appResources.getDisplayMetrics(), appResources.getConfiguration());

            //根据皮肤包文件，获取包名
            skinPackageName = application.getPackageManager()
                    .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;

            //无法获取皮肤包应用的包名，加载app内置资源
            isDefaultSkin = TextUtils.isEmpty(skinPackageName);

            Log.e("SKIN", skinPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            //预判：通过api可能发生了异常 设置加载默认资源
            isDefaultSkin = true;
        }
    }

    /**
     * 参考：resources.arsc资源映射表
     * 通过ID值获取资源的Name和Type
     *
     * @param resourceId 资源的ID值（app内置资源）
     * @return 如有没有皮肤包则加载app内置资源I，反之则加载皮肤包对应的资源ID
     */
    private int getSkinResourceIds(int resourceId) {
        String resourceName = appResources.getResourceEntryName(resourceId);
        String resourceType = appResources.getResourceTypeName(resourceId);

        //动态获取皮肤包内的指定资源ID
        int skinResourceId = skinResources.getIdentifier(resourceName, resourceType, skinPackageName);
        isDefaultSkin = skinResourceId == 0;
        return skinResourceId == 0 ? resourceId : skinResourceId;
    }

    public boolean isDefaultSkin() {
        return isDefaultSkin;
    }

}
