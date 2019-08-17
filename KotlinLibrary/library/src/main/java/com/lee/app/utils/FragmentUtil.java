package com.lee.app.utils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/**
 * @author Administrator
 * @date 2018/4/17
 */

public class FragmentUtil {

    private static FragmentUtil instance;

    private FragmentUtil() {
    }

    public static FragmentUtil getInstance() {
        if (instance == null) {
            synchronized (FragmentUtil.class) {
                if (instance == null) {
                    instance = new FragmentUtil();
                }
            }
        }
        return instance;
    }

    public void showFragmentDialog(FragmentActivity activity, Bundle bundle, DialogFragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if (fragment.isAdded()) {
            transaction.remove(fragment);
        }
        fragment.setArguments(bundle);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragment.show(transaction, fragment.getClass().getSimpleName());
    }

}
