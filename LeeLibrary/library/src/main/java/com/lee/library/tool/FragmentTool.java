package com.lee.library.tool;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;

/**
 * @author jv.lee
 * Created by Administrator on 2018/4/17.
 */

public class FragmentTool {

    private static FragmentTool instance;

    private FragmentTool(){}
    public static FragmentTool getInstance(){
        if (instance == null) {
            synchronized (FragmentTool.class) {
                if (instance == null) {
                    instance = new FragmentTool();
                }
            }
        }
        return instance;
    }

    public void showFragmentDialog(FragmentTransaction transaction, Bundle bundle, DialogFragment fragment) {
        fragment.setArguments(bundle);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragment.show(transaction, "df");
    }
}
