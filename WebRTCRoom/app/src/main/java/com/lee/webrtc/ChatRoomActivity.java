package com.lee.webrtc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.lee.webrtc.permission.PermissionManager;
import com.lee.webrtc.permission.PermissionRequest;


/**
 * @author jv.lee
 * @date 2019-07-31
 * @description
 */
public class ChatRoomActivity extends FragmentActivity {

    private FrameLayout frameVideoView;
    private WebRTCManager webRTCManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        initView();
    }

    private void initView() {
        frameVideoView = findViewById(R.id.frame_video_view);
        frameVideoView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        webRTCManager = WebRTCManager.getInstance();

        PermissionManager.getInstance()
                .attach(this)
                .request(Manifest.permission.CAMERA)
                .listener(new PermissionRequest() {
                    @Override
                    public void onPermissionSuccess() {
                        webRTCManager.joinRoom(this);
                    }

                    @Override
                    public void onPermissionFiled(String permission) {

                    }
                });

    }

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, ChatRoomActivity.class);
        activity.startActivity(intent);
    }

}
