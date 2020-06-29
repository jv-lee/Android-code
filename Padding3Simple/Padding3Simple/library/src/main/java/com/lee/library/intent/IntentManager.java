package com.lee.library.intent;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author jv.lee
 * @date 2019/4/14
 */
public class IntentManager {
    private static IntentManager instance;

    private IntentManager() {
    }

    public static IntentManager getInstance() {
        if (instance == null) {
            synchronized (IntentManager.class) {
                if (instance == null) {
                    instance = new IntentManager();
                }
            }
        }
        return instance;
    }

    public static final int REQUEST_CODE = 1001;
    public static final int RESULT_CODE = 1002;
    private static final String INTENT_TAG = "intent";
    public IntentRequest intentRequest;
    public IntentFragment fragment;


    public void startAct(Activity activity, Class<?> clazz) {
        startAct(activity, clazz, null);
    }

    public void startAct(Activity activity, Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
    }

    public void startAct(Activity activity, Class<?> clazz, Bundle bundle, int enterAnim, int exitAnim) {
        startAct(activity, clazz, bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }


    public void startActForResult(FragmentActivity activity, Class<?> clazz, IntentRequest intentRequest) {
        startActForResult(activity, clazz, null, intentRequest);
    }

    public void startActForResult(FragmentActivity activity, Class<?> clazz, Bundle bundle, IntentRequest intentRequest) {
        this.intentRequest = intentRequest;
        FragmentManager manager = activity.getSupportFragmentManager();
        if (fragment == null) {
            fragment = new IntentFragment();
        }
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (fragment.isAdded()) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.add(fragment, INTENT_TAG);
        fragmentTransaction.addToBackStack(INTENT_TAG);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commitAllowingStateLoss();
        manager.executePendingTransactions();

        Intent intent = new Intent(activity, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        fragment.startActivityForResult(intent, REQUEST_CODE);
    }

    public void startActForResult(FragmentActivity activity, Intent intent, IntentRequest intentRequest) {
        this.intentRequest = intentRequest;
        FragmentManager manager = activity.getSupportFragmentManager();
        if (fragment == null) {
            fragment = new IntentFragment();
        }
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (fragment.isAdded()) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.add(fragment, INTENT_TAG);
        fragmentTransaction.addToBackStack(INTENT_TAG);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commitAllowingStateLoss();
        manager.executePendingTransactions();

        fragment.startActivityForResult(intent, REQUEST_CODE);
    }

}
