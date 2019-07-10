package com.lee.component.personal.debug;

import android.os.Bundle;
import android.util.Log;

import com.lee.component.personal.R;
import com.lee.library.base.BaseActivity;
import com.lee.library.utils.Constants;

/**
 * @author jv.lee
 */
public class Personal_DebugActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity_debug);
        Log.e(Constants.TAG, "common/Personal_DebugActivity");
    }
}
