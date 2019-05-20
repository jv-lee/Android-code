package com.lee.library.intent;

import android.content.Intent;

/**
 * @author jv.lee
 * @date 2019/4/14
 */
public interface IntentRequest {

    /**
     * startForResult回调返回值
     * @param requestCode
     * @param resultCode
     * @param data
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
