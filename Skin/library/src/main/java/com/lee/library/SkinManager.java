package com.lee.library;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.lee.library.model.SkinCache;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
    private Map<String, SkinCache> cacheSkin;

    private static final String ADD_ASSET_PATH = "addAssetPath";

    private SkinManager(Application application) {
        this.application = application;
        appResources = application.getResources();
        cacheSkin = new HashMap<>();
    }

    public static SkinManager init(Application application) {
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
    public void loaderSkinResources(String skinPath) {
        //如果没有皮肤包或者没做换肤动作，方法不执行
        if (TextUtils.isEmpty(skinPath)) {
            isDefaultSkin = true;
            Log.e(">>>", "isDefaultSkin = true");
            return;
        }

        //优化：app冷启动、热启动可以获取缓存对象
        if (cacheSkin.containsKey(skinPath)) {
            isDefaultSkin = false;
            SkinCache skinCache = cacheSkin.get(skinPath);
            if (null != skinCache) {
                skinResources = skinCache.getSkinResources();
                skinPackageName = skinCache.getSkinPackageName();
                return;
            }
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
        //优化：如果没有皮肤包或者没有做换肤动作，直接返回app内置资源
        if (isDefaultSkin) {
            return resourceId;
        }

        String resourceName = appResources.getResourceEntryName(resourceId);
        String resourceType = appResources.getResourceTypeName(resourceId);

        //动态获取皮肤包内的指定资源ID
        int skinResourceId = skinResources.getIdentifier(resourceName, resourceType, skinPackageName);

        //源码第1924行：（0 is not a valid resourceID. )
        isDefaultSkin = skinResourceId == 0;
        return skinResourceId == 0 ? resourceId : skinResourceId;
    }

    public boolean isDefaultSkin() {
        return isDefaultSkin;
    }


    public int getColor(int resourceId) {
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin ? appResources.getColor(ids) : skinResources.getColor(ids);
    }

    public ColorStateList getColorStateList(int resourceId) {
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin ? appResources.getColorStateList(ids) : skinResources.getColorStateList(ids);
    }

    /**
     * mipmap和drawable统一用法（待测）
     * @param resourceId
     * @return
     */
    public Drawable getDrawableOrMipMap(int resourceId) {
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin ? appResources.getDrawable(ids) : skinResources.getDrawable(ids);
    }

    public String getString(int resourceId) {
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin ? appResources.getString(ids) : skinResources.getString(ids);
    }

    /**
     * 返回值特殊情况：可能是color / drawable / mipmap
     * @param resourceId 资源id
     * @return 返回相应的对象
     */
    public Object getBackgroundOrSrc(int resourceId) {
        // 需要获取当前属性的类型名Resources.getResourceTypeName(resourceId)再判断
        String resourceTypeName = appResources.getResourceTypeName(resourceId);

        switch (resourceTypeName) {
            case "color":
                return getColor(resourceId);
            // drawable / mipmap
            case "mipmap":
            case "drawable":
                return getDrawableOrMipMap(resourceId);
            default:
        }
        return null;
    }

    // 获得字体
    public Typeface getTypeface(int resourceId) {
        // 通过资源ID获取资源path，参考：resources.arsc资源映射表
        String skinTypefacePath = getString(resourceId);
        // 路径为空，使用系统默认字体
        if (TextUtils.isEmpty(skinTypefacePath)) {
            return Typeface.DEFAULT;
        }
        return isDefaultSkin ? Typeface.createFromAsset(appResources.getAssets(), skinTypefacePath)
                : Typeface.createFromAsset(skinResources.getAssets(), skinTypefacePath);
    }

}
