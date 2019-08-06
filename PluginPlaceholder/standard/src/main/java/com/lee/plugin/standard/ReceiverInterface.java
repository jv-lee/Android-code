package com.lee.plugin.standard;

import android.content.Context;
import android.content.Intent;

/**
 * @author jv.lee
 * @date 2019-08-06
 * @description
 */
public interface ReceiverInterface {
    void onReceive(Context context, Intent intent);
}
