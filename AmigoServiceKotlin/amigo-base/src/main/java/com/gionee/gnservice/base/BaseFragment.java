package com.gionee.gnservice.base;

import android.app.Fragment;
import android.view.View;

import com.gionee.gnservice.sdk.AmigoServiceSdk;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.SdkUtil;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    protected IAppContext mAppContext;

    private void initAppContext() {
        if (SdkUtil.isCallBySdk(getActivity())) {
            mAppContext = AmigoServiceSdk.getInstance().appContext();
        } else {
            mAppContext = (IAppContext) getActivity().getApplication();
        }
        LogUtil.d(TAG, "init Appcontext is null:" + (mAppContext == null));
    }

    protected IAppContext appContext() {
        initAppContext();
        return mAppContext;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(View view, int id) {
        return (T) view.findViewById(id);
    }

}
