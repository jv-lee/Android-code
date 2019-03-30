package com.lee.library.livedatabus;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * @author jv.lee
 * @date 2019/3/30
 */
public class HolderFragment extends Fragment {
    private int activityCode;
    private LifecycleListener lifecycleListener;

    public void setLifecycleListener(LifecycleListener lifecycleListener) {
        this.lifecycleListener = lifecycleListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null) {
            activityCode = getActivity().hashCode();
            if (lifecycleListener != null) {
                lifecycleListener.onCreate(activityCode);
            }
        }else{
            throw new IllegalStateException("activityCode = null");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (lifecycleListener != null) {
            lifecycleListener.onDetach(activityCode);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (lifecycleListener != null) {
            lifecycleListener.onStart(activityCode);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lifecycleListener != null) {
            lifecycleListener.onPause(activityCode);
        }
    }


}
