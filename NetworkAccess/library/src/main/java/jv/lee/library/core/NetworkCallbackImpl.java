package jv.lee.library.core;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import jv.lee.library.utils.Constants;

/**
 * @author jv.lee
 * @date 2019/9/23.
 * @description
 */
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        Log.d(Constants.LOG_TAG, "网络连接:" + network.toString());
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        Log.d(Constants.LOG_TAG, "网络丢失:" + network.toString());
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            //网络发生了变更
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i(Constants.LOG_TAG, "网络发生了变更，当前网络类型是wifi");
            } else {
                Log.i(Constants.LOG_TAG, "网络发生了变更，当前网络类型是");
            }
        }
    }

    @Override
    public void onUnavailable() {
        super.onUnavailable();
    }
}
