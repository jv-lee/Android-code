package com.lee.webrtc;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 *
 * @author jv.lee
 */
public class ChatRoomFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {


    Switch switchMute, switchEffect, switchCameraEnable, switchCameraDevice, switchClose;

    public ChatRoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_room, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        if (getView() == null) {
            return;
        }
        switchMute = getView().findViewById(R.id.switch_mute);
        switchEffect = getView().findViewById(R.id.switch_effect);
        switchCameraEnable = getView().findViewById(R.id.switch_camera_enable);
        switchCameraDevice = getView().findViewById(R.id.switch_camera_device);
        switchClose = getView().findViewById(R.id.switch_close);

        switchMute.setOnCheckedChangeListener(this);
        switchEffect.setOnCheckedChangeListener(this);
        switchCameraEnable.setOnCheckedChangeListener(this);
        switchCameraDevice.setOnCheckedChangeListener(this);
        switchClose.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ChatRoomActivity activity = null;
        if (getActivity() instanceof ChatRoomActivity) {
            activity = (ChatRoomActivity) getActivity();
        }
        if (activity == null) {
            return;
        }
        switch (buttonView.getId()) {
            //静音
            case R.id.switch_mute:
                activity.toggleMic(isChecked);
                break;
            //免提
            case R.id.switch_effect:
                activity.toggleLarge(isChecked);
                break;
            //摄像头开关
            case R.id.switch_camera_enable:
                activity.toggleCameraEnable(isChecked);
                break;
            //切换摄像头
            case R.id.switch_camera_device:
                activity.toggleCameraDevice(isChecked);
                break;
            //关闭通话
            case R.id.switch_close:
                activity.closeSession();
                break;
            default:
        }
    }
}
