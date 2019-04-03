package com.lee.code.messenger;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 进程通信类 封装了aidl功能 缺点 只能单线程
     */
    Messenger messenger;
    ServiceConnection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //连接后拿到远程服务实列放入messenger中通信
                Log.i("lee >>>", "onServiceConnected");
                messenger = new Messenger(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("lee >>>", "onServiceDisconnected");
            }
        };
        //绑定远程服务 指定包名 类名
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.lee.client", "com.lee.client.RemoteService"));
        intent.setAction("myaction");
        boolean bindFlag = this.getApplicationContext().bindService(intent, connection, BIND_AUTO_CREATE);
        Log.i("lee >>>", "bindFlag:" + bindFlag);

        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToClient(v);
            }
        });
    }

    public void sendMessageToClient(View view) {
        Message message = Message.obtain();
        message.replyTo = replyMessenger;
        Bundle bundle = new Bundle();
        bundle.putString("data","我要和Client通信");
        message.setData(bundle);
        //通过messenger把消息发送给远程服务
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    Messenger replyMessenger = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(MainActivity.this, msg.getData().getString("data"), Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
        }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
