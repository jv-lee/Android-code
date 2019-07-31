package com.lee.webrtc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    EditText etNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etNumber = findViewById(R.id.et_number);
    }

    public void JoinRoom(View view) {
        WebRTCManager.getInstance().connect(this, etNumber.getText().toString());
    }

}
