package com.lee.code.bsdiff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     *
     * @param oldApk 旧版本安装包 如1.1.1 版本的路径
     * @param patch 差分包，patch文件
     * @param output 合成之后的新文件，新版本apk的输出/保存路径
     */
    public native String bsPatch(String oldApk, String patch, String output);

}
