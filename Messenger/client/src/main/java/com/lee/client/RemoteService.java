package com.lee.client;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

/**
 * @author jv.lee
 * @date 2019/4/2
 */
public class RemoteService extends Service {
    @SuppressLint("HandlerLeak")
    Messenger messenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(RemoteService.this, msg.getData().getString("data"), Toast.LENGTH_SHORT).show();
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("data","客户端收到");
            message.setData(bundle);

            try {
                //获取主APP的 messenger对象 发送消息回去
                msg.replyTo.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "绑定成功", Toast.LENGTH_SHORT).show();
        return messenger.getBinder();
    }
}
