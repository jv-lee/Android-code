package com.gionee.gnservice.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.gionee.gnservice.sdk.AmigoServiceSdk;
import com.gionee.gnservice.sdk.INightModeHelper;

import java.lang.reflect.Field;

/**
 * 通过name来获取id,用户中心sdk需要使用
 */
public final class ResourceUtil {
    private static final String TAG = ResourceUtil.class.getSimpleName();
    private static final String TYPE_WIDGET = "id";
    private static final String TYPE_STRING = "string";
    private static final String TYPE_MENU = "menu";
    private static final String TYPE_LAYOUT = "layout";
    private static final String TYPE_DRAWABLE = "drawable";
    private static final String TYPE_DIMEN = "dimen";
    private static final String TYPE_COLOR = "color";
    private static final String TYPE_STYLE = "style";
    private static final String TYPE_ANIM = "anim";
    private static final String TYPE_STYLEABLE = "styleable";

    private ResourceUtil() {
    }

    public static int getResourceId(Context context, String resName, String type) {
        Resources res = context.getResources();
        String packageName = context.getPackageName();
        int id = res.getIdentifier(resName, type, packageName);
        if (id == 0) {
            LogUtil.d(TAG, "res " + resName + " not found");
            throw new RuntimeException("Resource " + resName + " not found");
        }
        return id;
    }

    public static int getWidgetId(Context context, String name) {
        return getResourceId(context, name, TYPE_WIDGET);
    }

    public static int getStringId(Context context, String name) {
        return getResourceId(context, name, TYPE_STRING);
    }

    public static String getString(Context context, String resName) {
        return context.getString(getStringId(context, resName));
    }

    public static int getMenuId(Context context, String name) {
        return getResourceId(context, name, TYPE_MENU);
    }

    public static int getLayoutId(Context context, String name) {
        int layoutId = getResourceId(context, name, TYPE_LAYOUT);
        if (AmigoServiceSdk.getInstance().isNightMode() && SdkUtil.isCallBySdk(context)) {
            String nightModeLayoutName = name + INightModeHelper.LAYOUT_SUFIX;
            int nightModeLayoutId = 0;
            try {
                nightModeLayoutId = getResourceId(context, nightModeLayoutName, TYPE_LAYOUT);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.d(TAG, e.getMessage());
            }
            if (nightModeLayoutId != 0) {
                return nightModeLayoutId;
            }
        }
        return layoutId;
    }

    public static int getDrawableId(Context context, String name) {
        return getResourceId(context, name, TYPE_DRAWABLE);
    }

    @TargetApi(21)
    public static Drawable getDrawable(Context context, String resName) {
        return context.getDrawable(getDrawableId(context, resName));
    }

    public static int getDimenId(Context context, String name) {
        return getResourceId(context, name, TYPE_DIMEN);
    }

    public static int getColorId(Context context, String name) {
        return getResourceId(context, name, TYPE_COLOR);
    }

    public static int getColor(Context context, String resName) {
        return context.getResources().getColor(getColorId(context, resName));
    }

    public static int getStyleId(Context context, String name) {
        int styleId = getResourceId(context, name, TYPE_STYLE);
        if (AmigoServiceSdk.getInstance().isNightMode() && SdkUtil.isCallBySdk(context)) {
            String nightModeStyleName = name + INightModeHelper.LAYOUT_SUFIX;
            int nightModeLayoutId = 0;
            try {
                nightModeLayoutId = getResourceId(context, nightModeStyleName, TYPE_STYLE);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.d(TAG, e.getMessage());
            }
            if (nightModeLayoutId != 0) {
                return nightModeLayoutId;
            }
        }
        return styleId;
    }

    public static int getAnimId(Context context, String name) {
        return getResourceId(context, name, TYPE_ANIM);
    }

    /**
     * 对于 context.getResources().getIdentifier 无法获取的数据 , 或者数组
     * 资源反射值
     */

    private static Object getResResourceId(Context context, String name, String type) {
        String className = context.getPackageName() + ".R";
        try {
            Class<?> cls = Class.forName(className);
            for (Class<?> childClass : cls.getClasses()) {
                String simple = childClass.getSimpleName();
                if (simple.equals(type)) {
                    for (Field field : childClass.getFields()) {
                        String fieldName = field.getName();
                        if (fieldName.equals(name)) {
                            LogUtil.d(TAG, "get res resource name is:" + name);
                            return field.get(null);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static int getStyleable(Context context, String name) {
        Object object = getResResourceId(context, name, TYPE_STYLEABLE);
        if (object == null) {
            return -1;
        }
        return (Integer) object;
    }


    public static int[] getStyleableArray(Context context, String name) {
        Object object = getResResourceId(context, name, TYPE_STYLEABLE);
        if (object == null) {
            return null;
        }
        return (int[]) object;
    }
}
