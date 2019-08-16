package com.gionee.gnservice.module.setting.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import com.gionee.gnservice.R;
import com.gionee.gnservice.UserCenterActivity;
import com.gionee.gnservice.module.setting.MemberSettingActivity;
import com.gionee.gnservice.utils.LogUtil;


///**
// * Created by borney on 11/2/16.
// */
public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "SERVICE_PUSH";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtil.d(TAG, "onReceive action = " + action);

//        EmptyService.keepApplication(context);

        if ("com.gionee.cloud.intent.REGISTRATION".equals(action)) {
            String rid;
            // Register RID success
            if (intent.getStringExtra("registration_id") != null) {
                rid = intent.getStringExtra("registration_id");
                LogUtil.d(TAG, "onReceive rid = " + rid);
//                ReceiverNotifier.getInstance().notifyRidGot(rid);
//                new InfoSendThread(context, rid).start();
                PushHelper.writeRid(context, rid);

                // Cancel RID success
            } else if (intent.getStringExtra("cancel_RID") != null) {
                LogUtil.d(TAG, "onReceive extra = cancel_RID");
//                rid = intent.getStringExtra("cancel_RID");
//                ReceiverNotifier.getInstance().notifyRidCanceled(rid);
//                EmptyService.cancelKeep(context);
                PushHelper.writeRid(context, "-1");
                // Failed on Registering RID or Canceling RID.
            } else if (intent.getStringExtra("error") != null) {
                LogUtil.d(TAG, "onReceive extra = error");
//                String error = intent.getStringExtra("error");
//                ReceiverNotifier.getInstance().notifyRidError(error);
//                EmptyService.cancelKeep(context);
            }

            // Have received push messages.
        } else if ("com.gionee.cloud.intent.RECEIVE".equals(action)) {
            String message = intent.getStringExtra("message");
            LogUtil.d(TAG, "recive push messge=" + message);
//            ReceiverNotifier.getInstance().notifyMessage(message);
//            EmptyService.cancelKeep(context);
            sendNotification(context.getApplicationContext(), message);
        }
    }

    public void sendNotification(Context context, String message) {
        boolean enable = MemberSettingActivity.isNotifySwitchEnable(context);
        if (!enable) {
            LogUtil.d("notify is disable,not send notification");
            return;
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, UserCenterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.drawable.uc_ic_push_notify_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.uc_ic_push_notify_icon))
                .setAutoCancel(true)
                .setContentTitle(context.getResources().getString(R.string.uc_user_center))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notify);

    }

}
