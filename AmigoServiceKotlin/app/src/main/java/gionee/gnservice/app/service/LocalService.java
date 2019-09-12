package gionee.gnservice.app.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import com.gionee.gnservice.statistics.StatisticsUtil;
import com.lee.library.utils.ConstUtil;
import gionee.gnservice.app.R;
import gionee.gnservice.app.tool.CalendarUtil;
import gionee.gnservice.app.tool.CommonTool;
import gionee.gnservice.app.view.activity.SplashActivity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author jv.lee
 */
public class LocalService extends Service {

    private static final String TAG = "LocalService";
    private static final int NOTIFICATION_ID = 1024;
    private static final String NOTIFICATION_CHANNEL = "default";
    private static final int NOTIFICATION_HOUR = 8;
    private static final int NOTIFICATION_MINUTE = 30;
    private static final int ALERT_HOUR = 12;
    private static final int ALERT_MINUTE = 0;

    private static final String ALERT_TITLE = "【用户中心】签到提醒";
    private static final String ALERT_DESCRIPTION = "用户中心每日签到，邀你一起瓜分百万现金红包";

    /**
     * 线程池 计时器
     */
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        initAlert();
        initNotification();
        bindService(new Intent(LocalService.this, RemoteService.class), mConnection, Service.BIND_IMPORTANT);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: ");
            startService(new Intent(LocalService.this, RemoteService.class));
            bindService(new Intent(LocalService.this, RemoteService.class), mConnection, Service.BIND_IMPORTANT);
        }
    };

    private void initAlert() {
        executorTimeTask(ALERT_HOUR, ALERT_MINUTE, ConstUtil.DAY, this::buildCalendar);
    }

    private void initNotification() {
        executorTimeTask(NOTIFICATION_HOUR, NOTIFICATION_MINUTE, ConstUtil.DAY, this::buildNotification);
    }

    private void buildCalendar() {
        CalendarUtil.addCalendarEvent(getApplicationContext(), ALERT_TITLE, ALERT_DESCRIPTION, System.currentTimeMillis());
    }

    private void buildNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(LocalService.this, NOTIFICATION_CHANNEL);
        } else {
            builder = new Notification.Builder(LocalService.this);
        }
        builder.setSmallIcon(R.mipmap.ic_launcher_sub)
                .setContent(getRemoteViews())
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(false)
                .setAutoCancel(true);

        Intent resultIntent = new Intent(LocalService.this, SplashActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra("notification", 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(LocalService.this, 5, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        StatisticsUtil.onEvent(LocalService.this, "Client_Notification", "创建");
    }

    private RemoteViews getRemoteViews() {
        return new RemoteViews(getPackageName(), R.layout.layout_notification);
    }

    private void executorTimeTask(int hour, int minute, int delay, Runnable runnable) {
        //每天下午 4:25  (目标时间减去当前时间 ：getTime会自定判断时间比当前小递增一天 时间或多一天  再减去 当前时间就获得了 剩下的小时时间)
        long executorTime = CommonTool.Companion.getTime(hour, minute) - System.currentTimeMillis();
        executor.scheduleAtFixedRate(runnable, executorTime, delay, TimeUnit.MILLISECONDS);
    }

}
