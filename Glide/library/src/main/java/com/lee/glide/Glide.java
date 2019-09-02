package com.lee.glide;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * @author jv.lee
 * @date 2019/9/2.
 * @description
 */
public class Glide {

    private RequestManagerRetriever retriever;

    Glide(RequestManagerRetriever retriever) {
        this.retriever = retriever;
    }

    public static RequestManager with(Context context) {
        return getRetriever(context).get(context);
    }

    public static RequestManager with(Activity activity) {
        return getRetriever(activity).get(activity);
    }

    public static RequestManager with(FragmentActivity activity) {
        return getRetriever(activity).get(activity);
    }

    public static RequestManager with(Fragment fragment) {
        return getRetriever(fragment.getActivity()).get(fragment);
    }

    public static RequestManager with(android.app.Fragment fragment) {
        return getRetriever(fragment.getActivity()).get(fragment);
    }

    public static RequestManager with(View view) {
        return getRetriever(view.getContext()).get(view);
    }


    private static RequestManagerRetriever getRetriever(Context context) {
        return Glide.get(context).getRetriever();
    }

    private static Glide get(Context context) {
        return new GlideBuilder().build();
    }

    private RequestManagerRetriever getRetriever() {
        return retriever;
    }

}
