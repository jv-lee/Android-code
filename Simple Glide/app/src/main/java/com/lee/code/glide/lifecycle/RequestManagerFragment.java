package com.lee.code.glide.lifecycle;

import android.app.Activity;
import android.app.Fragment;

public class RequestManagerFragment extends Fragment {
    private int activityCode;
    LifecycleObservable lifecycleObservable = LifecycleObservable.getInstance();

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityCode = activity.hashCode();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lifecycleObservable.onDestroy(activityCode);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
