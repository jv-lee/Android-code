package com.gionee.gnservice.statistics;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by zhongyx on 11/22/17.
 */

public class YouJuStatistics {

    private static final String TAG = "YouJuStatistics";
    static Class<?> sYouJuAgent = null;
    static Method sOnEventId;
    static Method sOnEventIdLabel;
    static Method sOnEventIdLabelData;

    static {
        try {
            sYouJuAgent = Class.forName("com.youju.statistics.YouJuAgent");
        } catch (Exception e) {
            try {
                sYouJuAgent = Class.forName("com.gionee.youju.statistics.sdk.YouJuAgent");
            } catch (Exception e2) {
                Log.d(TAG, "get ota.YouJuAgent " + e2.toString());
            }
        }
        if (sYouJuAgent != null) {
            initMethod();
        }
    }

    public static void init(Context context) {
        if (sYouJuAgent == null) {
            return;
        }

        try {
            Method init = sYouJuAgent.getMethod("init", Context.class);
            if (context != null) {
                Log.i(TAG, "init context != null");
            }
            init.invoke(sYouJuAgent, context);
        } catch (Exception e) {
            Log.d(TAG, "get YouJuAgent init method" + e.toString());
        }
    }

    public static void onEvent(Context context, String eventId) {
        if (sOnEventId == null) {
            return;
        }
        try {
            sOnEventId.invoke(sYouJuAgent, context, eventId);
        } catch (Exception e) {
            Log.d(TAG, "get YouJuAgent on event method" + e.toString());
        }
    }

    public static void onEvent(Context context, String eventId, String eventLabel) {
        if (sOnEventIdLabel == null) {
            return;
        }
        try {
            sOnEventIdLabel.invoke(sYouJuAgent, context, eventId, eventLabel);
        } catch (Exception e) {
            Log.d(TAG, "get YouJuAgent on event method" + e.toString());
        }
    }

    public static void onEvent(Context context, String eventId, String eventLabel,
                               Map<String, Object> map) {
        if (sOnEventIdLabelData == null) {
            return;
        }
        try {
            sOnEventIdLabelData.invoke(sYouJuAgent, context, eventId, eventLabel, map);
        } catch (Exception e) {
            Log.d(TAG, "execute on event method3 " + e.toString());
        }
    }

    private static void initMethod() {
        try {
            sOnEventId = sYouJuAgent.getMethod("onEvent", Context.class, String.class);
        } catch (Exception e) {
            Log.d(TAG, "get YouJuAgent on event method" + e.toString());
        }

        try {
            sOnEventIdLabel = sYouJuAgent.getMethod("onEvent", Context.class, String.class, String.class);
        } catch (Exception e) {
            Log.d(TAG, "get YouJuAgent on event method" + e.toString());
        }

        try {
            sOnEventIdLabelData = sYouJuAgent.getMethod("onEvent", Context.class, String.class,
                    String.class, Map.class);
        } catch (Exception e) {
            Log.d(TAG, "get YouJuAgent on event method3 " + e.toString());
        }
    }

}
