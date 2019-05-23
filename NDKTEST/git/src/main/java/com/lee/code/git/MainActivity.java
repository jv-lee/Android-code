package com.lee.code.git;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private ImageView ivImage;
    private Button btnLoadGif;

    private Bitmap bitmap;
    GifHandler gifHandler;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //刷新下一帧 重复刷新
            int nextFrame = gifHandler.updateFrame(bitmap);
            handler.sendEmptyMessageDelayed(1, nextFrame);
            ivImage.setImageBitmap(bitmap);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivImage = findViewById(R.id.iv_image);
        btnLoadGif = findViewById(R.id.btn_load_gif);

        btnLoadGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory(), "demo.gif");
                gifHandler = new GifHandler(file.getAbsolutePath());

                //获取gif宽高
                int width = gifHandler.getWidth();
                int height = gifHandler.getHeight();

                //创建bitmap
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                //返回值是下一帧的刷新时间
                int nextFrame = gifHandler.updateFrame(bitmap);
                //通知延迟刷新view
                handler.sendEmptyMessageDelayed(1, nextFrame);
            }
        });
    }
}
