package com.lee.webrtc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    EditText etSignal, etPort, etRoom, etWss;
    Button btnAdd, btnPointToVideo, btnPointToAudio, btnWss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        etSignal = findViewById(R.id.et_signal);
        etPort = findViewById(R.id.et_port);
        etRoom = findViewById(R.id.et_room);
        etWss = findViewById(R.id.et_wss);
        btnAdd = findViewById(R.id.btn_add);
        btnPointToVideo = findViewById(R.id.btn_point_to_video);
        btnPointToAudio = findViewById(R.id.btn_point_to_audio);
        btnWss = findViewById(R.id.btn_wss);
    }


    public void JoinRoom(View view) {
        WebRTCManager.getInstance().connect(this, etRoom.getText().toString());
    }

}
