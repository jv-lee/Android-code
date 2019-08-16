package com.gionee.gnservice.statistics;

import android.content.Context;
import android.util.Log;

import java.util.Map;

/**
 * Created by zhongyx on 11/22/17.
 */

public class StatisticsUtil {

    private static boolean sIsUploadSwitchOn = true;

    public static void init(Context context) {
        if (YouJuStatistics.sYouJuAgent != null) {
            YouJuStatistics.init(context.getApplicationContext());
        }
    }

    public static void onEvent(Context context, String eventId) {
        if (context == null) {
            return;
        }

        if (sIsUploadSwitchOn) {
            Log.i("HCLW", eventId);
            YouJuStatistics.onEvent(context.getApplicationContext(), eventId);
        }
    }

    public static void onEvent(Context context, String eventId, String eventLabel) {
        if (context == null) {
            return;
        }

        if (sIsUploadSwitchOn) {
            Log.i("HCLW", eventId +"____"+eventLabel);
            YouJuStatistics.onEvent(context.getApplicationContext(), eventId, eventLabel);
        }
    }

    public static void onEvent(Context context, String eventId, String eventLabel,
                               Map<String, Object> map) {
        if (context == null) {
            return;
        }

        if (sIsUploadSwitchOn) {
            Log.i("HCLW", eventId +"____"+eventLabel);
            YouJuStatistics.onEvent(context.getApplicationContext(), eventId, eventLabel, map);
        }
    }

    public static void setUploadSwitch(boolean couldUpload) {
        sIsUploadSwitchOn = couldUpload;
    }

    public static boolean isUploadSwitchOn() {
        return sIsUploadSwitchOn;
    }

}
