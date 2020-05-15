package com.lee.code.uidraw;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.lee.code.uidraw.view.FloatMoveImageView;

/**
 * @author jv.lee
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.view_input_password);

        setContentView(R.layout.activity_main);
        final FloatMoveImageView moveView = findViewById(R.id.move);
        findViewById(R.id.btn_mode_reindex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveView.setOverMode(FloatMoveImageView.OVER_MODE_REINDEX);
            }
        });
        findViewById(R.id.btn_mode_pullover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveView.setOverMode(FloatMoveImageView.OVER_MODE_PULLOVER);
            }
        });
        findViewById(R.id.btn_mode_hover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveView.setOverMode(FloatMoveImageView.OVER_MODE_HOVER);
            }
        });


//        setContentView(new PathView(this));
//        setContentView(new CarView(this));
//        setContentView(new MapView(this));
//        setContentView(new SplitView(this));
//        setContentView(new ColorFilterView(this));
//        setContentView(new TreeView(this));
//        setContentView(new CanvasTestView(this));
//        setContentView(new SplashView(this));
//        TouchMoveView view = findViewById(R.id.move);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
//            }
//        });

//        InputPasswordView inputPasswordView = findViewById(R.id.input_password);
//        inputPasswordView.setFocusable(true);
//        inputPasswordView.setFocusableInTouchMode(true);
//        inputPasswordView.requestFocus();
//        inputPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                Toast.makeText(MainActivity.this, "hasFocus:"+hasFocus, Toast.LENGTH_SHORT).show();
//            }
//        });

//        InputPasswordView passwordView = findViewById(R.id.input_password);
//        passwordView.openInput(this);
//        passwordView.setPasswordChange(new InputPasswordView.PasswordChange() {
//            @Override
//            public void onChange(Integer password) {
//                Toast.makeText(MainActivity.this, "当前输入的密码:" + password, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
