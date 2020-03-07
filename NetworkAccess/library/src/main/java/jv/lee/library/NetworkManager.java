package jv.lee.library;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import jv.lee.library.core.NetworkCallbackImpl;
import jv.lee.library.listener.NetChangeObserver;
import jv.lee.library.utils.Constants;

/**
 * @author jv.lee
 * @date 2019/9/23.
 * @description
 */
public class NetworkManager {

    /**
     * 同步锁，轻量级，变量或者属性发生重排序
     */
    private static volatile NetworkManager instance;

    private Application application;
    private NetStateReceiver receiver;

    private NetworkManager() {
        receiver = new NetStateReceiver();
    }

    public static NetworkManager getDefault() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }
        return instance;
    }

    @SuppressLint("MissingPermission")
    public void init(Application application) {
        this.application = application;

        //动态的广播注册(7.0+兼容)
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
        application.registerReceiver(receiver, filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager.NetworkCallback callback = new NetworkCallbackImpl();
            NetworkRequest request = new NetworkRequest.Builder().build();
            ConnectivityManager connManager = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager != null) {
                connManager.registerNetworkCallback(request, callback);
            }
        }
    }

    public void registerObserver(NetChangeObserver observer) {
        if (observer != null) {
            receiver.registerObserver(observer);
        }
    }

    public void unRegisterObserver(NetChangeObserver observer) {
        if (observer != null) {
            receiver.unRegisterObserver(observer);
        }
    }

    public Application getApplication() {
        if (application == null) {
            throw new RuntimeException("Application == null");
        }
        return application;
    }

    public void unRegister() {
        if (application != null && receiver != null) {
            application.unregisterReceiver(receiver);
        }
    }

}
