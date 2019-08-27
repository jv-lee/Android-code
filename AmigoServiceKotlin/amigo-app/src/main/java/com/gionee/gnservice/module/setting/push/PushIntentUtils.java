package com.gionee.gnservice.module.setting.push;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.google.gson.Gson;

public class PushIntentUtils {

    public static Intent getIntent(Context context, PushEntity pushEntity, Intent parentIntent) {
        Intent intent = null;
        switch (pushEntity.getAct()) {
            //打开H5页面
            case 1:
                intent = new Intent();
                intent.setComponent(new ComponentName("com.gionee.gnservice", "gionee.gnservice.app.view.activity.WebActivity"));
                intent.putExtra("url", pushEntity.getData().getUrl());
                intent.putExtra("vtype", pushEntity.getData().getVtype());
                intent.putExtra("title", pushEntity.getData().getTitle());
                intent.putExtra("sign", pushEntity.getData().isSign());
                break;
            //打开某个功能activity
            case 2:
                intent = new Intent();
                String packageName = pushEntity.getData().getActivity();
                intent.setComponent(new ComponentName(context.getPackageName(), packageName));
                break;
            //打开scheme
            case 3:
                intent = new Intent();
                intent.setData(Uri.parse(pushEntity.getData().getScheme()));
                break;
            //打开首页 跳转指定子页面
            case 4:
                intent = new Intent();
                intent.setComponent(new ComponentName("com.gionee.gnservice", "gionee.gnservice.app.view.activity.MainActivity"));
                intent.putExtra("vtype", pushEntity.getData().getVtype());
                intent.putExtra(PushReceiver.REGISTERATION_KEY_TITLE, pushEntity.getTitle());
                break;
            default:
                break;
        }
        assert intent != null;
        return putSystemParams(parentIntent, intent);
    }

    private static Intent putSystemParams(Intent parentIntent, Intent intent) {
        Log.i(">>>", "messageID:" + parentIntent.getStringExtra(PushReceiver.REGISTERATION_KEY_MESSAGE_ID));
        Log.i(">>>", "RID:" + parentIntent.getStringExtra("ridMsg"));
        intent.putExtra(PushReceiver.REGISTERATION_KEY_MESSAGE_ID, parentIntent.getStringExtra(PushReceiver.REGISTERATION_KEY_MESSAGE_ID));
        intent.putExtra(PushReceiver.REGISTERATION_KEY_RID, parentIntent.getStringExtra("ridMsg"));
        intent.putExtra(PushReceiver.REGISTERATION_KEY_CREATE_TIME, String.valueOf(System.currentTimeMillis()));
        return intent;
    }

    public static PushEntity getEntity(String data) {
        PushEntity pushEntity = new Gson().fromJson(data, PushEntity.class);
        return pushEntity;
    }

}
