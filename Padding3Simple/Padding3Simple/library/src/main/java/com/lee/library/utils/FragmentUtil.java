package com.lee.library.utils;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

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
