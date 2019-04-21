package com.lee.library.utils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Administrator on 2018/4/17.
 */

public class FragmentUtil {

    public static void showFragmentDialog(FragmentTransaction transaction, Bundle bundle, DialogFragment fragment) {
        fragment.setArguments(bundle);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragment.show(transaction, "df");
    }
}
