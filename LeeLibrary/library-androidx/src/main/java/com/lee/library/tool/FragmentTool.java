package com.lee.library.tool;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

/**
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
