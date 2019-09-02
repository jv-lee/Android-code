package com.lee.glide;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.Objects;

/**
 * @author jv.lee
 * @date 2019/9/2.
 * @description 管理RequestManager
 */
class RequestManagerRetriever {
    RequestManager get(Context context) {
        return new RequestManager(context);
    }

    RequestManager get(Activity activity) {
        return new RequestManager(activity);
    }

    RequestManager get(FragmentActivity activity) {
        return new RequestManager(activity);
    }

    RequestManager get(Fragment fragment) {
        return new RequestManager(Objects.requireNonNull(fragment.getActivity()));
    }

    RequestManager get(android.app.Fragment fragment) {
        return new RequestManager(fragment.getActivity());
    }

    RequestManager get(View view) {
        return new RequestManager(view.getContext());
    }
}
