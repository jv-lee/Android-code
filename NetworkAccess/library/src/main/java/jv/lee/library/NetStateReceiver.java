package jv.lee.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import jv.lee.library.listener.NetChangeObserver;
import jv.lee.library.type.NetType;
import jv.lee.library.utils.Constants;
import jv.lee.library.utils.NetworkUtils;

/**
 * @author jv.lee
 * @date 2019/9/23.
 * @description 网络状态监听广播
 */
public class NetStateReceiver extends BroadcastReceiver {
    private NetType netType;
    private final ArrayList<NetChangeObserver> mNetChangeObservers =
            new ArrayList<>();

    public NetStateReceiver() {
        //初始化
        netType = NetType.NONE;
    }

    public void registerObserver(NetChangeObserver observer) {
        synchronized (mNetChangeObservers) {
            mNetChangeObservers.add(observer);
        }
    }

    public void unRegisterObserver(NetChangeObserver observer) {
        synchronized (mNetChangeObservers) {
            mNetChangeObservers.remove(observer);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(Constants.LOG_TAG, "意图参数错误");
            return;
        }

        //处理广播事件
        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)) {
            Log.d(Constants.LOG_TAG, "网络发生了变更");
            //具体网络类型
            netType = NetworkUtils.getNetType();

            if (NetworkUtils.isNetworkAvailable()) {
                Log.d(Constants.LOG_TAG, "网络连接成功");
                for (NetChangeObserver observer : mNetChangeObservers) {
                    observer.onConnect(netType);
                }
            } else {
                Log.d(Constants.LOG_TAG, "网络连接失败");
                for (NetChangeObserver observer : mNetChangeObservers) {
                    observer.onDisConnect();
                }
            }
        }

    }
}
