package code.lee.client;

import android.aidl.DataEntity;
import android.aidl.IDataManager;
import android.aidl.IOnDataArrivedListener;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    IDataManager dataManager;
    List<DataEntity> list;

    /**
     * 远程连接
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dataManager = IDataManager.Stub.asInterface(service);
            try {
                //添加死亡重连机制当前重连是在binder线程池中操作的
                dataManager.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            try {
                dataManager.registerListener(mIOnDataArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            dataManager = null;
            Log.i("lee >>>", "远程服务断开连接");

            //断线重连 在ui线程中执行
            bindServer();
        }
    };


    /**
     * 回调接口
     */
    private IOnDataArrivedListener mIOnDataArrivedListener = new IOnDataArrivedListener.Stub() {
        @Override
        public void onDataArrivedListener(DataEntity entity) throws RemoteException {
            //切换到ui线程进行操作
            Log.i("lee >>>", entity.getName());
        }
    };

    /**
     * 死亡回调
     */
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (dataManager == null) {
                return;
            }
            //先滞空对象 再重新连连接
            dataManager.asBinder().unlinkToDeath(deathRecipient, 0);
            dataManager = null;
            //断线重连
            bindServer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定远程服务
        bindServer();
        findViewById(R.id.btn_find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAll();
            }
        });
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    private void bindServer() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.lee.server", "com.lee.server.DataManagerService"));
        intent.setAction("data_manager_service");
        boolean connFlag = bindService(intent, connection, BIND_AUTO_CREATE);
        Log.i("lee >>>", "connFlag:" + connFlag);
    }

    @Override
    protected void onDestroy() {
        //远程管理服务 不等于空 且属于连接状态 注销监听器
        if (dataManager != null && dataManager.asBinder().isBinderAlive()) {
            try {
                dataManager.unRegisterListener(mIOnDataArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(connection);
        super.onDestroy();
    }

    int i = 0;

    public void add() {
        if (dataManager != null) {
            try {
                dataManager.add(new DataEntity("3" + 0, "React Native " + i));
                i++;
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.i("lee >>>", "dataManager :" + e.getMessage());
            }
        } else {
            Log.i("lee >>>", "dataManager -> null");
        }
    }

    /**
     * 同理 如果是耗时操作 请放在子线程操作 切换ui线程来更新ui
     */
    public void findAll() {
        if (dataManager != null) {
            try {
                list = dataManager.findAll();
                for (DataEntity dataEntity : list) {
                    Log.i("lee >>>", dataEntity.getName());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.i("lee >>>", "dataManager :" + e.getMessage());
            }
        } else {
            Log.i("lee >>>", "dataManager -> null");
        }
    }

}
