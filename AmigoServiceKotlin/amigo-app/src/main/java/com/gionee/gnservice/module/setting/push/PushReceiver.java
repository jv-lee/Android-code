package com.gionee.gnservice.module.setting.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.gionee.gnservice.R;
import com.gionee.gnservice.module.setting.MemberSettingActivity;
import com.gionee.gnservice.utils.LogUtil;


///**
// * Created by borney on 11/2/16.
// */
public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "SERVICE_PUSH";
    public static final String REGISTERATION_KEY_PACKAGENAME = "packagename";
    public static final String REGISTERATION_KEY_MESSAGE_ID = "messageId";
    public static final String REGISTERATION_KEY_ERROR = "error";
    public static final String REGISTERATION_KEY_CLICK_TIME = "clickTime";
    public static final String REGISTERATION_KEY_EFFECT = "effect";
    public static final String REGISTERATION_KEY_RID = "rid";
    public static final String REGISTERATION_KEY_TITLE = "title";
    public static final String REGISTERATION_KEY_CREATE_TIME = "createTime";

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
//                new InfoSendThread(context, rid).init();
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
            try {
                sendNotification(context.getApplicationContext(), intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendNotification(Context context, Intent parentIntent) {
        Log.i(">>>", parentIntent.toString());
        boolean enable = MemberSettingActivity.isNotifySwitchEnable(context);
        if (!enable) {
            LogUtil.d("notify is disable,not send notification");
            return;
        }
        String message = parentIntent.getStringExtra("message");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int requestCode = (int) System.currentTimeMillis();
        PushEntity entity = PushIntentUtils.getEntity(message);
        Intent intent = PushIntentUtils.getIntent(context, entity, parentIntent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, 0);
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.drawable.uc_ic_push_notify_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.uc_ic_push_notify_icon))
                .setAutoCancel(true)
                .setContentTitle(entity.getTitle())
                .setContentText(entity.getDes())
                .setContentIntent(pendingIntent)
                .build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(requestCode, notify);
    }

    public static void startSaveServer(Context context, String messageId) {
        Intent intent = new Intent("com.gionee.cloud.intent.SEND_RESULT");
        intent.setPackage("com.gionee.cloud.gpe");
        intent.putExtra(REGISTERATION_KEY_PACKAGENAME, context.getPackageName());
        intent.putExtra(REGISTERATION_KEY_MESSAGE_ID, messageId);
        intent.putExtra(REGISTERATION_KEY_ERROR, "");
        intent.putExtra(REGISTERATION_KEY_CLICK_TIME, System.currentTimeMillis());
        intent.putExtra(REGISTERATION_KEY_EFFECT, 1);
        intent.putExtra(REGISTERATION_KEY_RID, PushHelper.readRid(context));
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
