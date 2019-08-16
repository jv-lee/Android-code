package com.gionee.gnservice.sdk;

import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caocong on 5/23/17.
 */
class NightModeHelper implements INightModeHelper {
    private static final String TAG = NightModeHelper.class.getSimpleName();
    private List<OnNightModeChangeListener> mListeners;

    NightModeHelper() {
        mListeners = new ArrayList<OnNightModeChangeListener>();
    }

    @Override
    public void registerNightModeChangeListener(OnNightModeChangeListener listener) {
        PreconditionsUtil.checkNotNull(listener);
        if (!mListeners.contains(listener)) {
            LogUtil.d(TAG, "registered night mode change listener");
            mListeners.add(listener);
        }
    }

    @Override
    public void unRegisterNightModeChangeListener(OnNightModeChangeListener listener) {
        PreconditionsUtil.checkNotNull(listener);
        if (mListeners.contains(listener)) {
            LogUtil.d(TAG, "unRegistered night mode change listener");
            mListeners.remove(listener);
        }
    }

    public void notifyChanged(boolean isNightMode) {
        for (OnNightModeChangeListener listener : mListeners) {
            listener.onNightModeChanged(isNightMode);
        }
    }

}
