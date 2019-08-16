package com.gionee.gnservice.utils;

import android.content.Context;
import android.widget.Toast;

public final class ToastUtil {
    private ToastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static final boolean SHOW = true;

    private static Toast sIntance;

    @SuppressWarnings("unused")
    public static void showShort(Context context, CharSequence message) {
        if (SHOW) {
            showMessage(context, message, Toast.LENGTH_SHORT);
        }
    }

    @SuppressWarnings("unused")
    public static void showShort(Context context, int resId) {
        if (SHOW) {
            showMessage(context, context.getString(resId), Toast.LENGTH_SHORT);
        }
    }

    @SuppressWarnings("unused")
    public static void showLong(Context context, CharSequence message) {
        if (SHOW) {
            showMessage(context, message, Toast.LENGTH_LONG);
        }
    }

    public static void showLong(Context context, int resId) {
        if (SHOW) {
            showMessage(context, context.getString(resId), Toast.LENGTH_LONG);
        }
    }

    @SuppressWarnings("unused")
    public static void show(Context context, CharSequence message, int duration) {
        if (SHOW) {
            showMessage(context, message, duration);
        }
    }

    @SuppressWarnings("unused")
    public static void show(Context context, int resId, int duration) {
        if (SHOW) {
            showMessage(context, context.getString(resId), duration);
        }
    }

    private static void showMessage(Context context, CharSequence message, int duration) {
        if (sIntance != null) {
            sIntance.setText(message);
            sIntance.setDuration(duration);
        } else {
            sIntance = Toast.makeText(context.getApplicationContext(), message, duration);
        }
        sIntance.show();
    }


    public static boolean showNetWorkErrorToast(Context context) {
        boolean isConnected = NetworkUtil.isConnected(context);
        if (!isConnected) {
            ToastUtil.showLong(context, ResourceUtil.getString(context, "uc_network_exception"));
        }
        return !isConnected;
    }


}
