package code.lee.code.permission.intent;

import android.content.Intent;

/**
 * @author jv.lee
 * @date 2019/4/14
 */
public interface IntentRequest {
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
