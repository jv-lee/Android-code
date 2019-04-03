package com.jv.code.logincomponent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.jv.code.componentlib.service.ILoginService;

public class LoginService implements ILoginService {
    @Override
    public void launch(Context context, String targetClass) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(LoginActivity.EXT_TAG, targetClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public Fragment newUserInfoFragment(FragmentManager fragmentManager, int viewId, Bundle bundle) {
        UserInfoFragment fragment = new UserInfoFragment();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().add(viewId, fragment).commit();
        return fragment;
    }
}
