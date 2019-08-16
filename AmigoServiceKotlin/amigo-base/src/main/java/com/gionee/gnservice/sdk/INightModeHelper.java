package com.gionee.gnservice.sdk;


/**
 * Created by caocong on 5/23/17.
 */
public interface INightModeHelper {

    String LAYOUT_SUFIX = "_night_mode";

    interface OnNightModeChangeListener {
        void onNightModeChanged(boolean isNightMode);
    }

    void registerNightModeChangeListener(OnNightModeChangeListener listener);

    void unRegisterNightModeChangeListener(OnNightModeChangeListener listener);


}
