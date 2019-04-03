package com.lee.server;

import android.aidl.DataEntity;
import android.aidl.IDataManager;
import android.aidl.IOnDataArrivedListener;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author jv.lee
 * @date 2019/4/3
 */
public class DataManagerService extends Service {

    /**
     * CopyOnWriteArrayList 是并发的读写类  因为存在多个客户端连接服务端做读写操作 所以此类可以自动同步
     * 同类型的还有ConcurrentHashMap
     */
    private CopyOnWriteArrayList<DataEntity> mDataList = new CopyOnWriteArrayList<>();
    /**
     * 客户端绑定的监听器 RemoteCallbackList
     * 因为不同进程下 Listener对象传递过来会重新生成 不是同一个 所以使用通常的反注册方法是无法注销监听的
     * 所以通过RemoteCallbackList来存监听器 内部提供了注册和反注册方法
     * 反注册时会通过监听器底层binder实列的相同来找到监听器 来注销操作
     */
    private RemoteCallbackList<IOnDataArrivedListener> mListener = new RemoteCallbackList<>();

    /**
     * Atomic是自带锁的属性  在当前线程内执行时 任务未执行完之前 不允许其他线程改变该值 直到当前线程任务执行完毕
     */
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    private Binder mBinder = new IDataManager.Stub() {

//        @Override
//        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
//            int check = checkCallingPermission("com.lee.aidl.permission.ACCESS_BOOK_SERVICE");
//            if(check == PackageManager.PERMISSION_DENIED){
//                return false;
//            }
//            return super.onTransact(code, data, reply, flags);
//        }

        @Override
        public List<DataEntity> findAll() throws RemoteException {
            return mDataList;
        }

        @Override
        public void add(DataEntity entity) throws RemoteException {
            mDataList.add(entity);
        }

        @SuppressLint("NewApi")
        @Override
        public void registerListener(IOnDataArrivedListener listener) throws RemoteException {
            //判断当前监听器集合有没有添加当前客户端注册的listener 没有加入集合中
            boolean registerFlag = mListener.register(listener);
            Log.i("lee >>>", listener + "监听器注册结果：" + registerFlag);
            //这是唯一获取监听器数量的方法
            final int N = mListener.beginBroadcast();
            mListener.finishBroadcast();
            Log.i("lee >>>", "监听器当前数量："+N);
        }

        @SuppressLint("NewApi")
        @Override
        public void unRegisterListener(IOnDataArrivedListener listener) throws RemoteException {
            boolean unregisterFlag = mListener.unregister(listener);
            Log.i("lee >>>", listener + " 监听器注销结果:" + unregisterFlag);
            final int N = mListener.beginBroadcast();
            mListener.finishBroadcast();
            Log.i("lee >>>", "监听器当前数量："+N);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDataList.add(new DataEntity("1", "Android"));
        mDataList.add(new DataEntity("2", "Ios"));
        //开启线程每5秒添加一条数据
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    /**
     * 线程任务 每5秒钟 添加一条数据 再通过遍历监听器 通知数据更新
     */
    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            //死循环条件 当前service没关闭的情况下调用
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DataEntity entity = new DataEntity(String.valueOf(mDataList.size()+1), "new Data #"+(mDataList.size()+1));
                mDataList.add(entity);
                //获取当前监听器实例的数量 遍历 拿到监听器 通知客户端更新数据 关闭广播事务
                final int N = mListener.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    IOnDataArrivedListener listener = mListener.getBroadcastItem(i);
                    if (listener != null) {
                        try {
                            //如果是耗时操作 尽量放在子线程操作
                            listener.onDataArrivedListener(entity);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mListener.finishBroadcast();
            }
        }
    }

}
