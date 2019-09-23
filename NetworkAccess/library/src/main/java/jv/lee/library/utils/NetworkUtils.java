package jv.lee.library.utils;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import jv.lee.library.NetworkManager;
import jv.lee.library.type.NetType;

/**
 * @author jv.lee
 * @date 2019/9/23.
 * @description
 */
public class NetworkUtils {

    /**
     * 判断当前网络是否连接
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable() {
        ConnectivityManager connManager = (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null) {
            return false;
        }
        //返回所有网络信息
        NetworkInfo[] infos = connManager.getAllNetworkInfo();
        if (infos != null) {
            for (NetworkInfo info : infos) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前具体网络类型
     *
     * @return NetType 网络类型
     */
    @SuppressLint("MissingPermission")
    public static NetType getNetType() {
        ConnectivityManager connManager = (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null) {
            return NetType.NONE;
        }

        //获取当前网络连接信息
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NetType.NONE;
        }
        int nType = networkInfo.getType();

        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equalsIgnoreCase("cmnet")) {
                return NetType.CMNET;
            } else {
                return NetType.CMWAP;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        }
        return NetType.NONE;
    }

    /**
     * 打开网络设置界面
     *
     * @param context
     * @param requestCode
     */
    public static void openSettings(Context context, int requestCode) {
        Intent intent = new Intent("/");
//        ComponentName cm = new ComponentName("com.android.settings");
    }

}
