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
import com.lee.webrtc.utils.Utils;

import org.webrtc.EglBase;
import org.webrtc.MediaStream;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author jv.lee
 * @date 2019-07-31
 * @description
 */
public class ChatRoomActivity extends FragmentActivity {

    private FrameLayout frameVideoView;
    private WebRTCManager webRTCManager;
    private EglBase eglBase;
    private VideoTrack localVideoTrack;

    private Map<String, SurfaceViewRenderer> videoVideos = new HashMap<>();
    private List<String> persons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Utils.getInstance(this);
        initView();
    }

    private void initView() {
        eglBase = EglBase.create();

        frameVideoView = findViewById(R.id.frame_video_view);
        frameVideoView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        webRTCManager = WebRTCManager.getInstance();

        PermissionManager.getInstance()
                .attach(this)
                .request(Manifest.permission.CAMERA)
                .listener(new PermissionRequest() {
                    @Override
                    public void onPermissionSuccess() {
                        webRTCManager.joinRoom(ChatRoomActivity.this, eglBase);
                    }

                    @Override
                    public void onPermissionFiled(String permission) {

                    }
                });

    }

    /**
     * @param stream 本地多媒体流（音视频数据）
     * @param userId 服务器返回当前用户 userID
     */
    public void setLocalStream(MediaStream stream, String userId) {
        List<VideoTrack> videoTracks = stream.videoTracks;
        if (videoTracks.size() > 0) {
            localVideoTrack = videoTracks.get(0);
        }
        runOnUiThread(() -> {
            addView(userId, stream);
        });
    }

    /**
     * 刷新ui界面
     * 会议室有多少人会调用多少次 根据socket中回调会议室人数ID
     *
     * @param userId 用户ID
     * @param stream 音视频流 （包含本地，远端视频流）
     */
    private void addView(String userId, MediaStream stream) {
        //使用Surface封装类 采用webRtc
        SurfaceViewRenderer renderer = new SurfaceViewRenderer(this);
        //初始化SurfaceView
        renderer.init(eglBase.getEglBaseContext(), null);
        //设置缩放模式 SCALE_ASPECT_FILL (按照View的宽度 和 高度设置，  SCALE_ASPECT_FILL 按照摄像头预览的画面大小设置
        renderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        //设置摄像头翻转
        renderer.setMirror(true);
        //将摄像头的数据渲染到surfaceViewRender
        if (stream.videoTracks.size() > 0) {
            stream.videoTracks.get(0).addSink(renderer);
        }

        //会议室聊天 1 + N
        videoVideos.put(userId, renderer);
        persons.add(userId);

        //将SurfaceViewRenderer添加到FrameLayout 当前 width = 0 ， height = 0；
        frameVideoView.addView(renderer);

        //宽度和高度 size  = 1
        int size = videoVideos.size();
        for (int i = 0; i < size; i++) {
            String personId = persons.get(i);
            SurfaceViewRenderer renderer1 = videoVideos.get(personId);

            if (renderer1 != null) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.width = Utils.getInstance().getWidth(size);
                layoutParams.height = Utils.getInstance().getWidth(size);
                layoutParams.leftMargin = Utils.getInstance().getX(size, i);
                layoutParams.leftMargin = Utils.getInstance().getY(size, i);
                renderer1.setLayoutParams(layoutParams);
            }
        }
    }

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, ChatRoomActivity.class);
        activity.startActivity(intent);
    }

}
