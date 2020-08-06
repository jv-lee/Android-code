package com.lee.library.intent;

import android.content.Intent;

import androidx.fragment.app.Fragment;

/**
 * @author jv.lee
 * @date 2019/4/14
 */
public class IntentFragment extends Fragment {

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (IntentManager.getInstance().intentRequest != null) {
            IntentManager.getInstance().intentRequest.onActivityResult(requestCode,resultCode,data);
        }
    }
}
